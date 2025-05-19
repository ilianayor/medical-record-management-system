package com.example.medical.record.service;

import com.example.medical.record.domain.dto.doctor.DoctorWithMostSickLeavesDTO;
import com.example.medical.record.domain.dto.sickleave.MonthWithHighestSickLeaveDTO;
import com.example.medical.record.domain.dto.sickleave.SickLeaveDTO;
import com.example.medical.record.domain.dto.sickleave.CreateSickLeaveDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SickLeaveService {

    List<SickLeaveDTO> getAllSickLeaves();

    SickLeaveDTO getSickLeaveById(Long sickLeaveId);

    ResponseEntity<SickLeaveDTO> createSickLeave(CreateSickLeaveDTO createSickLeaveDTO);

    SickLeaveDTO updateSickLeave(Long sickLeaveId, CreateSickLeaveDTO createSickLeaveDTO);

    void deleteSickLeave(Long sickLeaveId);

    List<MonthWithHighestSickLeaveDTO> getMonthWithHighestSickLeave();

    List<DoctorWithMostSickLeavesDTO> getDoctorsWithMostSickLeaves();
}
