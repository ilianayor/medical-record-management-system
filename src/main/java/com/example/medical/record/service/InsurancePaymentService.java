package com.example.medical.record.service;

import com.example.medical.record.domain.dto.insurance.CreateInsurancePaymentDTO;
import com.example.medical.record.domain.dto.insurance.InsurancePaymentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InsurancePaymentService {

    List<InsurancePaymentDTO> getAllInsurancePayments();

    InsurancePaymentDTO getInsurancePaymentById(Long id);

    ResponseEntity<InsurancePaymentDTO> createInsurancePayment(
        CreateInsurancePaymentDTO createHealthInsuranceDTO);

    InsurancePaymentDTO updateInsurancePayment(Long id, CreateInsurancePaymentDTO createHealthInsuranceDTO);

    boolean hasHealthInsurance(Long patientId);

}
