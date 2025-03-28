package org.svrohith9.patientservice.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String birthDate;
    private String gender;
}
