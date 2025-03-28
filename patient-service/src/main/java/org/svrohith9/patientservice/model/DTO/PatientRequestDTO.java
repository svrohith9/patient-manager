package org.svrohith9.patientservice.model.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.svrohith9.patientservice.enums.Gender;

@Getter
@Setter
@ToString
public class PatientRequestDTO {
    @NotBlank(message = "First Name is Required")
    @Size(max = 100, message = "First Name cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last Name is Required")
    @Size(max = 100, message = "Last Name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotNull(message = "Registered date is required")
    private String registeredDate;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private String address;

    @NotNull(message = "Birth date is required")
    private String birthDate;


}
