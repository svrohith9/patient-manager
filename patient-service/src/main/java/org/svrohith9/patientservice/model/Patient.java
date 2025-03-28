package org.svrohith9.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.svrohith9.patientservice.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Setter
@Getter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String address;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private String phoneNumber;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private LocalDate registrationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;


}
