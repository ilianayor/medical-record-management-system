package com.example.medical.record.service;

import com.example.medical.record.domain.dto.specialty.CreateSpecialtyDTO;
import com.example.medical.record.domain.dto.specialty.SpecialtyDTO;
import com.example.medical.record.domain.entity.Specialty;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDTO> getAllSpecialties();

    SpecialtyDTO getSpecialtyById(Long specialtyId);

    ResponseEntity<SpecialtyDTO> createSpecialty(CreateSpecialtyDTO createSpecialtyDTO);

    SpecialtyDTO updateSpecialty(Long specialtyId, CreateSpecialtyDTO createSpecialtyDTO);

    void deleteSpecialty(Long specialtyId);

    Specialty getSpecialty(Long specialtyId);
}
