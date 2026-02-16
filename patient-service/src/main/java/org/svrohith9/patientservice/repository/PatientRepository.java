package org.svrohith9.patientservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.svrohith9.patientservice.model.Patient;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    Optional<Patient> findByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE p.isActive = :active")
    Page<Patient> findByActive(@Param("active") Boolean active, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.lastName) = LOWER(:lastName)")
    Page<Patient> findByLastNameIgnoreCase(@Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.isActive = true")
    long countActivePatients();
}
