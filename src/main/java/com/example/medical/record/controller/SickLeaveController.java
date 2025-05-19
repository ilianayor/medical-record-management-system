package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.doctor.DoctorWithMostSickLeavesDTO;
import com.example.medical.record.domain.dto.sickleave.MonthWithHighestSickLeaveDTO;
import com.example.medical.record.domain.dto.sickleave.SickLeaveDTO;
import com.example.medical.record.service.SickLeaveService;
import com.example.medical.record.domain.dto.sickleave.CreateSickLeaveDTO;
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
@RequestMapping("/sickLeaves")
@RequiredArgsConstructor
public class SickLeaveController {

    private final SickLeaveService sickLeaveService;

    @GetMapping
    public List<SickLeaveDTO> getAllSickLeaves() {
        return sickLeaveService.getAllSickLeaves();
    }

    @GetMapping("/{id}")
    public SickLeaveDTO getSickLeave(@PathVariable Long id) {
        return sickLeaveService.getSickLeaveById(id);
    }

    @PostMapping
    public ResponseEntity<SickLeaveDTO> createSickLeave(@Valid @RequestBody CreateSickLeaveDTO createSickLeaveDTO) {
        return sickLeaveService.createSickLeave(createSickLeaveDTO);
    }

    @PutMapping("/{id}")
    public SickLeaveDTO updateSickLeave(@PathVariable Long id, @RequestBody CreateSickLeaveDTO createSickLeaveDTO) {
        return sickLeaveService.updateSickLeave(id, createSickLeaveDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSickLeave(@PathVariable Long id) {
        sickLeaveService.deleteSickLeave(id);
    }

    @GetMapping("/highest-num-sick-leaves")
    public List<MonthWithHighestSickLeaveDTO> getMonthWithHighestSickLeave() {
        return sickLeaveService.getMonthWithHighestSickLeave();
    }

    @GetMapping("/doctor-most-sick-leaves")
    public List<DoctorWithMostSickLeavesDTO> getDoctorsWithMostSickLeaves() {
        return sickLeaveService.getDoctorsWithMostSickLeaves();
    }
}
