package com.example.medical.record.repository;

import com.example.medical.record.domain.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsBySpecialtyName(String specialtyName);
}
