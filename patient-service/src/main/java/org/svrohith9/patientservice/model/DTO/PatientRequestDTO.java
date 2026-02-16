package org.svrohith9.patientservice.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.svrohith9.patientservice.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Patient creation/update request")
public class PatientRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    @Schema(description = "Patient's first name", example = "Jane", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    @Schema(description = "Patient's last name", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    @Schema(description = "Patient's phone number", example = "+15551234567", required = true)
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Patient's email address", example = "jane.doe@example.com", required = true)
    private String email;

    @NotNull(message = "Registration date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Registration date", example = "2024-01-15", required = true)
    private String registeredDate;

    @NotNull(message = "Gender is required")
    @Schema(description = "Patient's gender", example = "FEMALE", required = true)
    private Gender gender;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    @Schema(description = "Patient's address", example = "456 Elm Street, Springfield, IL 62701", required = true)
    private String address;

    @NotNull(message = "Birth date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Birth date must be in the past")
    @Schema(description = "Patient's birth date", example = "1990-01-15", required = true)
    private String birthDate;

    @Schema(description = "Emergency contact number (optional)", example = "+15559876543")
    private String emergencyContact;

    @Schema(description = "Blood type (optional)", example = "O+")
    private String bloodType;
}
