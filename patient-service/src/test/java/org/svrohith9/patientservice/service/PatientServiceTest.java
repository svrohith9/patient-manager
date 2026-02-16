package org.svrohith9.patientservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.svrohith9.patientservice.exception.EmailAlreadyExistException;
import org.svrohith9.patientservice.exception.PatientNotFoundException;
import org.svrohith9.patientservice.grpc.BillingServiceGrpcClient;
import org.svrohith9.patientservice.kafka.KafkaProducer;
import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.model.Patient;
import org.svrohith9.patientservice.repository.PatientRepository;
import org.svrohith9.patientservice.enums.Gender;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private BillingServiceGrpcClient billingServiceGrpcClient;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private PatientService patientService;

    private PatientRequestDTO patientRequestDTO;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patientRequestDTO = new PatientRequestDTO();
        patientRequestDTO.setFirstName("Jane");
        patientRequestDTO.setLastName("Doe");
        patientRequestDTO.setEmail("jane.doe@example.com");
        patientRequestDTO.setPhoneNumber("+15551234567");
        patientRequestDTO.setAddress("456 Elm Street");
        patientRequestDTO.setGender(Gender.FEMALE);
        patientRequestDTO.setBirthDate("1990-01-15");
        patientRequestDTO.setRegisteredDate("2024-01-15");

        patient = Patient.builder()
                .id(UUID.randomUUID())
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phoneNumber("+15551234567")
                .address("456 Elm Street")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1990, 1, 15))
                .registrationDate(LocalDate.of(2024, 1, 15))
                .isActive(true)
                .build();
    }

    @Test
    void createPatient_Success() {
        when(patientRepository.existsByEmail(patientRequestDTO.getEmail())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientResponseDTO result = patientService.createPatient(patientRequestDTO);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("jane.doe@example.com", result.getEmail());
        verify(patientRepository).save(any(Patient.class));
        verify(kafkaProducer).sendEvent(any(Patient.class), eq("PATIENT_CREATED"));
    }

    @Test
    void createPatient_EmailAlreadyExists_ThrowsException() {
        when(patientRepository.existsByEmail(patientRequestDTO.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistException.class, 
                () -> patientService.createPatient(patientRequestDTO));

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getPatientById_Success() {
        UUID patientId = patient.getId();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        PatientResponseDTO result = patientService.getPatientById(patientId);

        assertNotNull(result);
        assertEquals(patient.getFirstName(), result.getFirstName());
    }

    @Test
    void getPatientById_NotFound_ThrowsException() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, 
                () -> patientService.getPatientById(patientId));
    }

    @Test
    void updatePatient_Success() {
        UUID patientId = patient.getId();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientResponseDTO result = patientService.updatePatient(patientId, patientRequestDTO);

        assertNotNull(result);
        verify(patientRepository).save(any(Patient.class));
        verify(kafkaProducer).sendEvent(any(Patient.class), eq("PATIENT_UPDATED"));
    }

    @Test
    void updatePatient_NotFound_ThrowsException() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, 
                () -> patientService.updatePatient(patientId, patientRequestDTO));
    }

    @Test
    void deletePatient_Success() {
        UUID patientId = patient.getId();
        when(patientRepository.existsById(patientId)).thenReturn(true);

        patientService.deletePatient(patientId);

        verify(patientRepository).deleteById(patientId);
    }

    @Test
    void deletePatient_NotFound_ThrowsException() {
        UUID patientId = UUID.randomUUID();
        when(patientRepository.existsById(patientId)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, 
                () -> patientService.deletePatient(patientId));
    }
}
