package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.doctor.DoctorWithMostSickLeavesDTO;
import com.example.medical.record.domain.dto.sickleave.MonthWithHighestSickLeaveDTO;
import com.example.medical.record.domain.dto.sickleave.SickLeaveDTO;
import com.example.medical.record.exception.InvalidDateRangeException;
import com.example.medical.record.exception.InvalidValidationException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.domain.dto.sickleave.CreateSickLeaveDTO;
import com.example.medical.record.domain.entity.MedicalVisit;
import com.example.medical.record.domain.entity.SickLeave;
import com.example.medical.record.repository.SickLeaveRepository;
import com.example.medical.record.service.InsurancePaymentService;
import com.example.medical.record.service.MedicalVisitService;
import com.example.medical.record.service.SickLeaveService;
import com.example.medical.record.util.MapperUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SickLeaveServiceImpl implements SickLeaveService {

    private final SickLeaveRepository sickLeaveRepository;

    private final MedicalVisitService medicalVisitService;
    private final InsurancePaymentService insurancePaymentService;

    private final MapperUtil mapperUtil;

    @Override
    public SickLeaveDTO getSickLeaveById(Long sickLeaveId) {
        SickLeave sickLeave = sickLeaveRepository.findById(sickLeaveId)
            .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Sick leave with id [%s] not found", sickLeaveId)));
        return mapperUtil.getModelMapper().map(sickLeave, SickLeaveDTO.class);
    }

    @Override
    public List<SickLeaveDTO> getAllSickLeaves() {
        List<SickLeave> sickLeaves = sickLeaveRepository.findAll();

        if (sickLeaves.isEmpty()) {
            throw new ObjectNotFoundException("Sick leaves not found");
        }

        return mapperUtil.mapList(sickLeaves, SickLeaveDTO.class);
    }

    @Override
    @Transactional
    public ResponseEntity<SickLeaveDTO> createSickLeave(CreateSickLeaveDTO createSickLeaveDTO) {
        MedicalVisit medicalVisit = medicalVisitService.getMedicalVisit(createSickLeaveDTO.getMedicalVisitId());

        if (medicalVisit.getSickLeave() != null) {
            throw new InvalidValidationException("Medical visit already has sick leave assigned");
        }

        Long patientId = medicalVisit.getPatient().getId();

        if (!insurancePaymentService.hasHealthInsurance(patientId)) {
            throw new InvalidValidationException(
                "Patient does not have valid health insurance. Sick leave cannot be granted.");
        }

        if (createSickLeaveDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new InvalidDateRangeException("Start date of sick leave cannot be in the past");
        }

        if (createSickLeaveDTO.getStartDate().isBefore(medicalVisit.getVisitDate())) {
            throw new InvalidDateRangeException("Start date of sick leave cannot be before medical visit date");
        }

        if (createSickLeaveDTO.getEndDate().isBefore(createSickLeaveDTO.getStartDate())) {
            throw new InvalidDateRangeException("End date of sick leave cannot be before start date");
        }

        SickLeave sickLeave = mapperUtil.getModelMapper().map(createSickLeaveDTO, SickLeave.class);
        sickLeave.setMedicalVisit(medicalVisit);

        SickLeave savedSickLeave = sickLeaveRepository.save(sickLeave);
        SickLeaveDTO sickLeaveDTO = mapperUtil.getModelMapper().map(savedSickLeave, SickLeaveDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(sickLeaveDTO);
    }

    @Override
    public SickLeaveDTO updateSickLeave(Long id, CreateSickLeaveDTO createSickLeaveDTO) {
        SickLeave sickLeave = sickLeaveRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Sick leave with id [%s] not found.", id)));

        mapperUtil.getModelMapper().map(createSickLeaveDTO, sickLeave);
        SickLeave updatedSickLeave = sickLeaveRepository.save(sickLeave);
        return mapperUtil.getModelMapper().map(updatedSickLeave, SickLeaveDTO.class);
    }

    @Override
    public void deleteSickLeave(Long id) {
        SickLeave sickLeave = sickLeaveRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(String.format("Sick leave with id [%s] not found.", id)));

        sickLeaveRepository.deleteById(id);
    }

    @Override
    public List<MonthWithHighestSickLeaveDTO> getMonthWithHighestSickLeave() {
        List<Object[]> results = sickLeaveRepository.findMonthsWithHighestSickLeave();

        return results.stream()
            .map(row -> new MonthWithHighestSickLeaveDTO(
                (int) row[0],
                (int) row[1],
                (long) row[2]
            ))
            .collect(Collectors.toList());
    }

    @Override
    public List<DoctorWithMostSickLeavesDTO> getDoctorsWithMostSickLeaves() {
        List<Object[]> results = sickLeaveRepository.findDoctorsWithMostSickLeaves();

        return results.stream()
            .map(row -> new DoctorWithMostSickLeavesDTO(
                (Long) row[0],
                (String) row[1],
                (Long) row[2]
            ))
            .collect(Collectors.toList());
    }
}
