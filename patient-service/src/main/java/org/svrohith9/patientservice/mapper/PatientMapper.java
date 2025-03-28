package org.svrohith9.patientservice.mapper;

import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO patientDTO = new PatientResponseDTO();
        patientDTO.setId(String.valueOf(patient.getId()));
        patientDTO.setFirstName(patient.getFirstName());
        patientDTO.setLastName(patient.getLastName());
        patientDTO.setGender(String.valueOf(patient.getGender()));
        patientDTO.setAddress(String.valueOf(patient.getAddress()));
        patientDTO.setBirthDate(String.valueOf(patient.getBirthDate()));
        patientDTO.setEmail(patient.getEmail());
        return patientDTO;
    }

    public static Patient toEntity(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setFirstName(patientRequestDTO.getFirstName());
        patient.setLastName(patientRequestDTO.getLastName());
        patient.setGender(patientRequestDTO.getGender());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setBirthDate(LocalDate.parse(patientRequestDTO.getBirthDate()));
        patient.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        patient.setPhoneNumber(String.valueOf(patientRequestDTO.getPhoneNumber()));
        patient.setEmail(patientRequestDTO.getEmail());
        return patient;
    }
}
