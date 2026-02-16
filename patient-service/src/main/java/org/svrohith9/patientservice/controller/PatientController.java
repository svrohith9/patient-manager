package org.svrohith9.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.svrohith9.patientservice.model.DTO.PatientRequestDTO;
import org.svrohith9.patientservice.model.DTO.PatientResponseDTO;
import org.svrohith9.patientservice.service.PatientService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@CrossOrigin(origins = {"*"})
@Tag(name = "Patient Management", description = "APIs for managing patient records")
@Validated
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @Operation(
            summary = "Get all patients",
            description = "Retrieves a paginated list of all patients. Supports pagination and sorting."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient list"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content)
    })
    public ResponseEntity<Page<PatientResponseDTO>> getPatients(
            @Parameter(description = "Page number (0-indexed)") 
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @Parameter(description = "Sort field and direction (e.g., 'lastName,asc')") 
            @RequestParam(defaultValue = "id,asc") String sort) {
        
        log.info("Fetching patients - page: {}, size: {}, sort: {}", page, size, sort);
        
        // Parse sort parameter
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<PatientResponseDTO> patients = patientService.getPatients(pageable);
        
        log.info("Retrieved {} patients, total: {}", patients.getNumberOfElements(), patients.getTotalElements());
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Get patient by ID",
            description = "Retrieves a specific patient by their unique identifier"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    public ResponseEntity<PatientResponseDTO> getPatientById(
            @Parameter(description = "Patient UUID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID uuid) {
        log.info("Fetching patient with ID: {}", uuid);
        PatientResponseDTO patient = patientService.getPatientById(uuid);
        return ResponseEntity.ok(patient);
    }

    @PostMapping
    @Operation(
            summary = "Create new patient",
            description = "Creates a new patient record. Automatically generates UUID and handles billing account creation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Patient with email already exists", content = @Content)
    })
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        log.info("Creating new patient with email: {}", patientRequestDTO.getEmail());
        PatientResponseDTO createdPatient = patientService.createPatient(patientRequestDTO);
        log.info("Patient created successfully with ID: {}", createdPatient.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PutMapping("/{uuid}")
    @Operation(
            summary = "Update patient",
            description = "Updates an existing patient record"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email already in use", content = @Content)
    })
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @Parameter(description = "Patient UUID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID uuid,
            @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        log.info("Updating patient with ID: {}", uuid);
        PatientResponseDTO updatedPatient = patientService.updatePatient(uuid, patientRequestDTO);
        log.info("Patient updated successfully: {}", uuid);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{uuid}")
    @Operation(
            summary = "Delete patient",
            description = "Deletes a patient record by UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "Patient UUID", required = true, schema = @Schema(type = "string", format = "uuid"))
            @PathVariable UUID uuid) {
        log.info("Deleting patient with ID: {}", uuid);
        patientService.deletePatient(uuid);
        log.info("Patient deleted successfully: {}", uuid);
        return ResponseEntity.noContent().build();
    }
}
