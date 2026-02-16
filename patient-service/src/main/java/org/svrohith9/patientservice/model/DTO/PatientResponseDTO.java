package org.svrohith9.patientservice.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Patient response")
public class PatientResponseDTO {

    @Schema(description = "Patient unique identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Patient's first name", example = "Jane")
    private String firstName;

    @Schema(description = "Patient's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Patient's email address", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Patient's phone number", example = "+15551234567")
    private String phoneNumber;

    @Schema(description = "Patient's address", example = "456 Elm Street, Springfield, IL 62701")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Patient's birth date", example = "1990-01-15")
    private String birthDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Patient's registration date", example = "2024-01-15")
    private String registeredDate;

    @Schema(description = "Patient's gender", example = "FEMALE")
    private String gender;

    @Schema(description = "Whether patient is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Emergency contact number", example = "+15559876543")
    private String emergencyContact;

    @Schema(description = "Blood type", example = "O+")
    private String bloodType;
}
