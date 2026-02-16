package org.svrohith9.patientservice.mapper;

import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    private PatientMapper() {
        // Utility class - no instantiation
    }

    public static PatientResponseDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        return PatientResponseDTO.builder()
                .id(patient.getId() != null ? patient.getId().toString() : null)
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .birthDate(patient.getBirthDate() != null ? patient.getBirthDate().toString() : null)
                .registeredDate(patient.getRegistrationDate() != null ? patient.getRegistrationDate().toString() : null)
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
                .isActive(patient.getIsActive())
                .emergencyContact(patient.getEmergencyContact())
                .bloodType(patient.getBloodType())
                .build();
    }

    public static Patient toEntity(PatientRequestDTO patientRequestDTO) {
        if (patientRequestDTO == null) {
            return null;
        }

        return Patient.builder()
                .firstName(patientRequestDTO.getFirstName())
                .lastName(patientRequestDTO.getLastName())
                .email(patientRequestDTO.getEmail())
                .phoneNumber(patientRequestDTO.getPhoneNumber())
                .address(patientRequestDTO.getAddress())
                .gender(patientRequestDTO.getGender())
                .birthDate(LocalDate.parse(patientRequestDTO.getBirthDate()))
                .registrationDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()))
                .emergencyContact(patientRequestDTO.getEmergencyContact())
                .bloodType(patientRequestDTO.getBloodType())
                .isActive(true)
                .build();
    }
}
