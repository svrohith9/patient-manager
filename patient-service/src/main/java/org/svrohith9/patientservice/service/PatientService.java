package org.svrohith9.patientservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.svrohith9.patientservice.exception.EmailAlreadyExistException;
import org.svrohith9.patientservice.exception.PatientNotFoundException;
import org.svrohith9.patientservice.mapper.PatientMapper;
import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.model.Patient;
import org.svrohith9.patientservice.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientDTOs = patients.stream().map(PatientMapper::toDTO).toList();
        return patientDTOs;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("Duplicate value detected, A Person exists with same email.");
        }
        Patient newPatient = patientRepository.save(PatientMapper.toEntity(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID uuid, PatientRequestDTO patientRequestDTO) {
        Patient updatedPatient = patientRepository.findById(uuid).orElseThrow(() -> new PatientNotFoundException("patient not found with ID :" + uuid));
        if (!updatedPatient.getEmail().equalsIgnoreCase(patientRequestDTO.getEmail()) && patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistException("Duplicate value detected, A Person exists with same email.");
        }

        updatedPatient.setEmail(patientRequestDTO.getEmail());
        updatedPatient.setFirstName(patientRequestDTO.getFirstName());
        updatedPatient.setLastName(patientRequestDTO.getLastName());
        updatedPatient.setGender(patientRequestDTO.getGender());
        updatedPatient.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));
        updatedPatient.setAddress(patientRequestDTO.getAddress());
        updatedPatient.setPhoneNumber(patientRequestDTO.getPhoneNumber());
        Patient patient = patientRepository.save(updatedPatient);
        return PatientMapper.toDTO(patient);

    }

    public void deletePatient(UUID uuid) {
        if (!patientRepository.existsById(uuid)) {
            throw new PatientNotFoundException("patient not found with ID :" + uuid);
        }
        patientRepository.deleteById(uuid);

    }

}
