package org.svrohith9.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/patients/v1")
@CrossOrigin(origins = {"*"})
@Tag(name = "Patient", description = "Patient Controller")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    @Operation(summary = "Create Patient")
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@Valid @PathVariable java.util.UUID uuid, @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.updatePatient(uuid, patientRequestDTO));
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = "Delete patient")
    public ResponseEntity<Void> deletePatient(@Valid @PathVariable java.util.UUID uuid) {
        patientService.deletePatient(uuid);
        return ResponseEntity.noContent().build();
    }
}
