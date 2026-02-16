package org.svrohith9.patientservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.svrohith9.patientservice.exception.EmailAlreadyExistException;
import org.svrohith9.patientservice.exception.PatientNotFoundException;
import org.svrohith9.patientservice.grpc.BillingServiceGrpcClient;
import org.svrohith9.patientservice.kafka.KafkaProducer;
import org.svrohith9.patientservice.mapper.PatientMapper;
import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.model.Patient;
import org.svrohith9.patientservice.repository.PatientRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository,
                         BillingServiceGrpcClient billingServiceGrpcClient,
                         KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public Page<PatientResponseDTO> getPatients(Pageable pageable) {
        log.debug("Fetching patients with pagination: {}", pageable);
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(PatientMapper::toDTO);
    }

    public PatientResponseDTO getPatientById(UUID uuid) {
        log.debug("Fetching patient by ID: {}", uuid);
        Patient patient = patientRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.warn("Patient not found with ID: {}", uuid);
                    return new PatientNotFoundException("Patient not found with ID: " + uuid);
                });
        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        log.debug("Creating patient with email: {}", patientRequestDTO.getEmail());
        
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            log.warn("Duplicate email attempt: {}", patientRequestDTO.getEmail());
            throw new EmailAlreadyExistException(
                    "A patient with email " + patientRequestDTO.getEmail() + " already exists"
            );
        }

        Patient newPatient = PatientMapper.toEntity(patientRequestDTO);
        Patient savedPatient = patientRepository.save(newPatient);
        log.info("Patient saved with ID: {}", savedPatient.getId());

        // Create billing account via gRPC (with circuit breaker)
        createBillingAccount(savedPatient);

        // Publish event to Kafka
        publishPatientEvent(savedPatient, "PATIENT_CREATED");

        return PatientMapper.toDTO(savedPatient);
    }

    @CircuitBreaker(name = "billingService", fallbackMethod = "billingServiceFallback")
    @Retry(name = "billingServiceRetry", maxAttempts = 3, backoff = @Backoff(delay = 1000))
    private void createBillingAccount(Patient patient) {
        log.debug("Creating billing account for patient: {}", patient.getId());
        billingServiceGrpcClient.createBillingAccount(
                String.valueOf(patient.getId()),
                patient.getFirstName() + " " + patient.getLastName(),
                patient.getEmail()
        );
        log.info("Billing account created for patient: {}", patient.getId());
    }

    private void billingServiceFallback(Patient patient, Throwable t) {
        log.error("Failed to create billing account for patient: {}. Error: {}", 
                  patient.getId(), t.getMessage());
        // Don't fail the patient creation if billing fails
        // In production, consider a compensation mechanism or outbox pattern
    }

    private void publishPatientEvent(Patient patient, String eventType) {
        try {
            kafkaProducer.sendEvent(patient, eventType);
            log.debug("Patient event published: {} for patient: {}", eventType, patient.getId());
        } catch (Exception e) {
            log.error("Failed to publish patient event for patient: {}. Error: {}", 
                      patient.getId(), e.getMessage());
            // Don't fail the patient creation if Kafka publish fails
        }
    }

    public PatientResponseDTO updatePatient(UUID uuid, PatientRequestDTO patientRequestDTO) {
        log.debug("Updating patient with ID: {}", uuid);
        
        Patient existingPatient = patientRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.warn("Patient not found for update: {}", uuid);
                    return new PatientNotFoundException("Patient not found with ID: " + uuid);
                });

        // Check for email conflict (only if email is changing)
        if (!existingPatient.getEmail().equalsIgnoreCase(patientRequestDTO.getEmail()) 
                && patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            log.warn("Email conflict during update: {}", patientRequestDTO.getEmail());
            throw new EmailAlreadyExistException(
                    "A patient with email " + patientRequestDTO.getEmail() + " already exists"
            );
        }

        // Update fields
        existingPatient.setFirstName(patientRequestDTO.getFirstName());
        existingPatient.setLastName(patientRequestDTO.getLastName());
        existingPatient.setEmail(patientRequestDTO.getEmail());
        existingPatient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        existingPatient.setAddress(patientRequestDTO.getAddress());
        existingPatient.setGender(patientRequestDTO.getGender());
        existingPatient.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));

        Patient updatedPatient = patientRepository.save(existingPatient);
        log.info("Patient updated successfully: {}", uuid);

        // Publish update event
        publishPatientEvent(updatedPatient, "PATIENT_UPDATED");

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID uuid) {
        log.debug("Deleting patient with ID: {}", uuid);
        
        if (!patientRepository.existsById(uuid)) {
            log.warn("Patient not found for deletion: {}", uuid);
            throw new PatientNotFoundException("Patient not found with ID: " + uuid);
        }

        patientRepository.deleteById(uuid);
        log.info("Patient deleted successfully: {}", uuid);

        // Publish deletion event
        try {
            Patient deletedPatient = Patient.builder()
                    .id(uuid)
                    .firstName("DELETED")
                    .lastName("DELETED")
                    .email("deleted@patient.local")
                    .build();
            kafkaProducer.sendEvent(deletedPatient, "PATIENT_DELETED");
        } catch (Exception e) {
            log.warn("Failed to publish deletion event for patient: {}. Error: {}", uuid, e.getMessage());
        }
    }
}
