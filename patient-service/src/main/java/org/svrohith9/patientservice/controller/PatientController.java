package org.svrohith9.patientservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/patients/v1")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@Valid @PathVariable java.util.UUID uuid, @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.updatePatient(uuid, patientRequestDTO));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePatient(@Valid @PathVariable java.util.UUID uuid) {
        patientService.deletePatient(uuid);
        return ResponseEntity.noContent().build();
    }
}
