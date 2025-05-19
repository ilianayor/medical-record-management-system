package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.treatment.CreateTreatmentDTO;
import com.example.medical.record.domain.dto.treatment.TreatmentDTO;
import com.example.medical.record.service.TreatmentService;
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
@RequiredArgsConstructor
@RequestMapping("/treatments")
public class TreatmentController {
    private final TreatmentService treatmentService;
    @GetMapping
    public List<TreatmentDTO> getAllTreatments() {
        return treatmentService.getAllTreatments();
    }

    @GetMapping("/{id}")
    public TreatmentDTO getTreatment(@PathVariable Long id) {
        return treatmentService.getTreatmentById(id);
    }

    @PostMapping
    public ResponseEntity<TreatmentDTO> createTreatment(@Valid @RequestBody CreateTreatmentDTO createTreatmentDTO) {
        return treatmentService.createTreatment(createTreatmentDTO);
    }

    @PutMapping("/{id}")
    public TreatmentDTO updateTreatment(@PathVariable Long id, @RequestBody CreateTreatmentDTO createTreatmentDTO) {
        return treatmentService.updateTreatment(id, createTreatmentDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTreatment(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
    }
}
