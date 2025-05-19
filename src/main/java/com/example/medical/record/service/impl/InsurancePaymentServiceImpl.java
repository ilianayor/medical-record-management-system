package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.insurance.CreateInsurancePaymentDTO;
import com.example.medical.record.domain.dto.insurance.InsurancePaymentDTO;
import com.example.medical.record.domain.entity.InsurancePayment;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.exception.InvalidValidationException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.InsurancePaymentRepository;
import com.example.medical.record.service.InsurancePaymentService;
import com.example.medical.record.service.PatientService;
import com.example.medical.record.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsurancePaymentServiceImpl implements InsurancePaymentService {

    private final InsurancePaymentRepository insurancePaymentRepository;

    private final PatientService patientService;

    private final MapperUtil mapperUtil;

    @Override
    public List<InsurancePaymentDTO> getAllInsurancePayments() {
        List<InsurancePayment> healthInsurancePayments = insurancePaymentRepository.findAll();
        if (healthInsurancePayments.isEmpty()) {
            throw new ObjectNotFoundException("No insurance payment found");
        }
        return mapperUtil.mapList(healthInsurancePayments, InsurancePaymentDTO.class);
    }

    @Override
    public InsurancePaymentDTO getInsurancePaymentById(Long id) {
        InsurancePayment healthInsurancePayment = insurancePaymentRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Health insurance payment not found"));

        return mapperUtil.getModelMapper().map(healthInsurancePayment, InsurancePaymentDTO.class);
    }

    @Override
    @Transactional
    public ResponseEntity<InsurancePaymentDTO> createInsurancePayment(
        CreateInsurancePaymentDTO createHealthInsuranceDTO) {

        Patient patient = patientService.getPatient(createHealthInsuranceDTO.getPatientId());

        LocalDate paymentDate = createHealthInsuranceDTO.getPaymentDate();

        if (paymentDate.isAfter(LocalDate.now())) {
            throw new InvalidValidationException("Payment date cannot be in the future");
        }

        InsurancePayment
            healthInsurancePayment = mapperUtil.getModelMapper().map(createHealthInsuranceDTO, InsurancePayment.class);
        healthInsurancePayment.setPatient(patient);

        InsurancePayment savedHealthInsurance = insurancePaymentRepository.save(healthInsurancePayment);

        InsurancePaymentDTO
            healthInsuranceDTO = mapperUtil.getModelMapper().map(savedHealthInsurance, InsurancePaymentDTO.class);

        return new ResponseEntity<>(healthInsuranceDTO, HttpStatus.CREATED);
    }

    @Override
    public InsurancePaymentDTO updateInsurancePayment(Long id,
                                                      CreateInsurancePaymentDTO createHealthInsuranceDTO) {
        InsurancePayment healthInsurancePayment = insurancePaymentRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Health insurance payment not found"));

        mapperUtil.getModelMapper().map(createHealthInsuranceDTO, healthInsurancePayment);
        InsurancePayment updatedHealthInsurancePayment = insurancePaymentRepository.save(healthInsurancePayment);

        return mapperUtil.getModelMapper().map(updatedHealthInsurancePayment, InsurancePaymentDTO.class);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR') or (hasAuthority('PATIENT') and #patientId == authentication.principal.getPatientId())")
    @Override
    public boolean hasHealthInsurance(Long patientId) {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        return insurancePaymentRepository.hasPaidHealthInsuranceInLastSixMonths(patientId, sixMonthsAgo);
    }
}
