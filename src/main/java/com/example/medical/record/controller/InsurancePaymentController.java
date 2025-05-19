package com.example.medical.record.controller;

import com.example.medical.record.domain.dto.insurance.CreateInsurancePaymentDTO;
import com.example.medical.record.domain.dto.insurance.InsurancePaymentDTO;
import com.example.medical.record.service.InsurancePaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/insurance")
public class InsurancePaymentController {

    private final InsurancePaymentService insurancePaymentService;

    @GetMapping
    public List<InsurancePaymentDTO> getAllAllInsurancePayments() {
        return insurancePaymentService.getAllInsurancePayments();
    }

    @GetMapping("/{id}")
    public InsurancePaymentDTO getInsurancePayment(@PathVariable Long id) {
        return insurancePaymentService.getInsurancePaymentById(id);
    }

    @PostMapping
    public ResponseEntity<InsurancePaymentDTO> createInsurancePayment(@Valid @RequestBody
                                                                      CreateInsurancePaymentDTO createHealthInsuranceDTO) {
        return insurancePaymentService.createInsurancePayment(createHealthInsuranceDTO);
    }

    @PutMapping("/{id}")
    public InsurancePaymentDTO updateHealthInsurancePayment(@RequestBody
                                                            CreateInsurancePaymentDTO createHealthInsuranceDTO,
                                                            @PathVariable Long id) {
        return insurancePaymentService.updateInsurancePayment(id, createHealthInsuranceDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @GetMapping("patient/{patientId}/has-health-insurance")
    public ResponseEntity<Boolean> checkHealthInsurance(@PathVariable Long patientId) {
        boolean isValid = insurancePaymentService.hasHealthInsurance(patientId);
        return ResponseEntity.ok(isValid);
    }
}
