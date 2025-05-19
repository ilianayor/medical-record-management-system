package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.specialty.CreateSpecialtyDTO;
import com.example.medical.record.domain.dto.specialty.SpecialtyDTO;
import com.example.medical.record.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    @GetMapping
    public List<SpecialtyDTO> getAllSpecialties() {
        return specialtyService.getAllSpecialties();
    }

    @GetMapping("/{id}")
    public SpecialtyDTO getSpecialty(@PathVariable Long id) {
        return specialtyService.getSpecialtyById(id);
    }

    @PostMapping
    public ResponseEntity<SpecialtyDTO> createSpecialty(@Valid @RequestBody CreateSpecialtyDTO createSpecialtyDTO) {
        return specialtyService.createSpecialty(createSpecialtyDTO);
    }

    @PutMapping("/{id}")
    public SpecialtyDTO updateSpecialty(@PathVariable Long id, @RequestBody CreateSpecialtyDTO createSpecialtyDTO) {
        return specialtyService.updateSpecialty(id, createSpecialtyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
    }

}
