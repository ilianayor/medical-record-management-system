package com.example.medical.record.service;

import com.example.medical.record.domain.dto.diagnosis.CreateDiagnosisDTO;
import com.example.medical.record.domain.dto.visit.CreateMedicalVisitDTO;
import com.example.medical.record.domain.dto.visit.MedicalVisitDTO;
import com.example.medical.record.domain.entity.Diagnosis;
import com.example.medical.record.domain.entity.Doctor;
import com.example.medical.record.domain.entity.MedicalVisit;
import com.example.medical.record.domain.entity.Patient;
import com.example.medical.record.exception.InvalidDateRangeException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.MedicalVisitRepository;
import com.example.medical.record.service.impl.MedicalVisitServiceImpl;
import com.example.medical.record.util.MapperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(SpringExtension.class)
@Configuration
public class MedicalVisitServiceImplUnitTest {

    @Mock
    private MedicalVisitRepository medicalVisitRepository;
    @InjectMocks
    private MedicalVisitServiceImpl medicalVisitService;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private DiagnosisService diagnosisService;

    @Mock
    private MapperUtil mapperUtil;

    @Mock
    private MedicalVisit medicalVisit;
    @Mock
    private MedicalVisitDTO medicalVisitDTO;
    @Mock
    private CreateMedicalVisitDTO createMedicalVisitDTO;
    @Mock
    private Doctor doctor;
    @Mock
    private Patient patient;
    @Mock
    private Diagnosis diagnosis;
    @Mock
    private ModelMapper modelMapper;

    private final Long medicalVisitId = 1L;

    @BeforeEach
    void setUp() {
        when(mapperUtil.getModelMapper()).thenReturn(modelMapper);
    }

    @Test
    void testGetMedicalVisitById_WhenMedicalVisitExists() {
        Long patientId = 1L;
        Long doctorId = 1L;

        when(medicalVisitRepository.findById(1L)).thenReturn(Optional.of(medicalVisit));
        when(modelMapper.map(medicalVisit, MedicalVisitDTO.class)).thenReturn(medicalVisitDTO);

        when(medicalVisitDTO.getMedicalVisitId()).thenReturn(medicalVisitId);
        when(medicalVisitDTO.getVisitDate()).thenReturn(LocalDate.now());
        when(medicalVisitDTO.getPatientId()).thenReturn(patientId);
        when(medicalVisitDTO.getDoctorId()).thenReturn(doctorId);

        MedicalVisitDTO result = medicalVisitService.getMedicalVisitById(medicalVisitId);

        assertEquals("Medical visit id should be as expected", medicalVisitDTO.getMedicalVisitId(),
            result.getMedicalVisitId());
        assertEquals("Visit date should be as expected", medicalVisitDTO.getVisitDate(), result.getVisitDate());
        assertEquals("Patient id should be as expected", medicalVisitDTO.getPatientId(), result.getPatientId());
        assertEquals("Doctor id should be as expected", medicalVisitDTO.getDoctorId(), result.getDoctorId());

        verify(medicalVisitRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMedicalVisitById_WhenMedicalVisitDoesNotExist() {
        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> medicalVisitService.getMedicalVisitById(medicalVisitId));

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
    }

    @Test
    void testGetAllMedicalVisits_WhenMedicalVisitsExist() {
        MedicalVisit medicalVisit2 = mock();
        MedicalVisitDTO medicalVisitDTO2 = mock();

        when(medicalVisit.getId()).thenReturn(medicalVisitId);
        when(medicalVisit.getVisitDate()).thenReturn(LocalDate.now());

        when(medicalVisit2.getId()).thenReturn(2L);
        when(medicalVisit2.getVisitDate()).thenReturn(LocalDate.now().plusDays(1));

        List<MedicalVisit> medicalVisits = List.of(medicalVisit, medicalVisit2);

        when(medicalVisitDTO.getMedicalVisitId()).thenReturn(medicalVisitId);
        when(medicalVisitDTO.getVisitDate()).thenReturn(LocalDate.now());

        when(medicalVisitDTO2.getMedicalVisitId()).thenReturn(2L);
        when(medicalVisitDTO2.getVisitDate()).thenReturn(LocalDate.now().plusDays(1));

        List<MedicalVisitDTO> medicalVisitDTOs = List.of(medicalVisitDTO, medicalVisitDTO2);

        when(medicalVisitRepository.findAll()).thenReturn(medicalVisits);
        when(mapperUtil.mapList(medicalVisits, MedicalVisitDTO.class)).thenReturn(medicalVisitDTOs);

        List<MedicalVisitDTO> result = medicalVisitService.getAllMedicalVisits();

        assertIterableEquals(medicalVisitDTOs, result);
        verify(medicalVisitRepository, times(1)).findAll();
    }

    @Test
    void testGetAllMedicalVisits_WhenMedicalVisitsDoesNotExist() {
        when(medicalVisitRepository.findAll()).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () -> medicalVisitService.getAllMedicalVisits());

        verify(medicalVisitRepository, times(1)).findAll();
    }

    @Test
    void testCreateMedicalVisit_ShouldReturnMedicalVisitDTO() {
        MedicalVisit savedVisit = mock();
        MedicalVisitDTO medicalVisitDTO = mock();

        when(createMedicalVisitDTO.getDoctorId()).thenReturn(1L);
        when(createMedicalVisitDTO.getPatientId()).thenReturn(1L);

        when(doctorService.getDoctor(1L)).thenReturn(doctor);
        when(patientService.getPatient(1L)).thenReturn(patient);

        when(modelMapper.map(createMedicalVisitDTO, MedicalVisit.class)).thenReturn(medicalVisit);
        when(medicalVisitRepository.save(medicalVisit)).thenReturn(savedVisit);
        when(modelMapper.map(savedVisit, MedicalVisitDTO.class)).thenReturn(medicalVisitDTO);

        ResponseEntity<MedicalVisitDTO> result = medicalVisitService.createMedicalVisit(createMedicalVisitDTO);

        assertEquals("Should return status code 201 CREATED", HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Should return as expected", medicalVisitDTO, result.getBody());

        verify(doctorService, times(1)).getDoctor(1L);
        verify(patientService, times(1)).getPatient(1L);
        verify(medicalVisitRepository, times(1)).save(medicalVisit);
    }

    @Test
    void testCreateMedicalVisit_WhenDoctorNotFound() {
        when(createMedicalVisitDTO.getDoctorId()).thenReturn(1L);
        when(doctorService.getDoctor(1L)).thenThrow(new ObjectNotFoundException("Doctor not found"));

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.createMedicalVisit(createMedicalVisitDTO),
            "Medical visit cannot be created. Doctor does not exist");

        verify(doctorService, times(1)).getDoctor(1L);
    }

    @Test
    void testCreateMedicalVisit_WhenPatientNotFound() {
        when(createMedicalVisitDTO.getDoctorId()).thenReturn(1L);
        when(createMedicalVisitDTO.getPatientId()).thenReturn(1L);
        when(doctorService.getDoctor(1L)).thenReturn(doctor);
        when(patientService.getPatient(1L)).thenThrow(new ObjectNotFoundException("Patient not found"));

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.createMedicalVisit(createMedicalVisitDTO),
            "Medical visit cannot be created. Patient does not exist");

        verify(doctorService, times(1)).getDoctor(1L);
        verify(patientService, times(1)).getPatient(1L);
    }

    @Test
    void testUpdateMedicalVisit_Successful() {
        MedicalVisit updatedMedicalVisit = mock();
        MedicalVisitDTO medicalVisitDTO = mock();

        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.of(medicalVisit));
        when(modelMapper.map(createMedicalVisitDTO, MedicalVisit.class)).thenReturn(medicalVisit);
        when(medicalVisitRepository.save(medicalVisit)).thenReturn(updatedMedicalVisit);
        when(modelMapper.map(updatedMedicalVisit, MedicalVisitDTO.class)).thenReturn(medicalVisitDTO);

        MedicalVisitDTO result = medicalVisitService.updateMedicalVisit(medicalVisitId, createMedicalVisitDTO);

        assertEquals("Result should be as expected", medicalVisitDTO, result);
        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
        verify(medicalVisitRepository, times(1)).save(medicalVisit);
    }

    @Test
    void testUpdateMedicalVisit_WhenMedicalVisitNotFound() {
        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.updateMedicalVisit(medicalVisitId, createMedicalVisitDTO),
            "Medical visit is not found and cannot be updated");

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
    }

    @Test
    void testDeleteMedicalVisit_WhenMedicalVisitExist() {
        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.of(medicalVisit));

        medicalVisitService.deleteMedicalVisit(medicalVisitId);

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
        verify(medicalVisitRepository, times(1)).deleteById(medicalVisitId);
    }

    @Test
    void testDeleteMedicalVisit_WhenMedicalVisitNotFound() {
        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> medicalVisitService.deleteMedicalVisit(medicalVisitId));

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
        verify(medicalVisitRepository, never()).deleteById(medicalVisitId);
    }

    @Test
    void testAddDiagnosisToMedicalVisit_Successful() {
        Long diagnosisId = 1L;

        CreateDiagnosisDTO createDiagnosisDTO = mock();
        Set<Diagnosis> diagnoses = new HashSet<>(Set.of(diagnosis));

        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.of(medicalVisit));
        when(diagnosisService.getDiagnosis(diagnosisId)).thenReturn(diagnosis);
        when(medicalVisit.getDiagnoses()).thenReturn(diagnoses);

        when(mapperUtil.getModelMapper().map(diagnosis, CreateDiagnosisDTO.class)).thenReturn(createDiagnosisDTO);

        List<CreateDiagnosisDTO> result = medicalVisitService.addDiagnosisToMedicalVisit(medicalVisitId, diagnosisId);

        assertNotNull(result);
        assertEquals("Result size should be 1", 1, result.size());
        assertEquals("Result should be as expected", createDiagnosisDTO, result.get(0));

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
        verify(diagnosisService, times(1)).getDiagnosis(diagnosisId);
        verify(medicalVisitRepository, times(1)).save(medicalVisit);
    }

    @Test
    void testAddDiagnosisToMedicalVisit_WhenMedicalVisitNotFound() {
        Long diagnosisId = 2L;

        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.addDiagnosisToMedicalVisit(medicalVisitId, diagnosisId),
            "Medical visit does not exist. Diagnosis cannot be assigned");

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
        verify(diagnosisService, never()).getDiagnosis(diagnosisId);
        verify(medicalVisitRepository, never()).save(any(MedicalVisit.class));
    }

    @Test
    void testAddDiagnosisToMedicalVisit_WhenDiagnosisNotFound() {
        Long medicalVisitId = 1L;
        Long diagnosisId = 2L;

        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.of(medicalVisit));
        when(diagnosisService.getDiagnosis(diagnosisId)).thenThrow(new ObjectNotFoundException("Diagnosis not found"));

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.addDiagnosisToMedicalVisit(medicalVisitId, diagnosisId),
            "Diagnosis does not exist");

        verify(medicalVisitRepository, times(1)).findById(medicalVisitId);
        verify(diagnosisService, times(1)).getDiagnosis(diagnosisId);
        verify(medicalVisitRepository, never()).save(any(MedicalVisit.class));
    }

    @Test
    void testGetVisitHistoryByPatient_Successful() {
        Long patientId = 1L;
        MedicalVisit medicalVisit2 = mock();
        CreateMedicalVisitDTO dto1 = mock();
        CreateMedicalVisitDTO dto2 = mock();
        List<MedicalVisit> visits = List.of(medicalVisit, medicalVisit2);

        List<CreateMedicalVisitDTO> medicalVisitDTOs = List.of(dto1, dto2);

        when(medicalVisitRepository.findByPatientId(patientId)).thenReturn(visits);
        when(mapperUtil.mapList(visits, CreateMedicalVisitDTO.class)).thenReturn(medicalVisitDTOs);

        List<CreateMedicalVisitDTO> result = medicalVisitService.getVisitHistoryByPatient(patientId);

        assertEquals("Result should be as expected", medicalVisitDTOs, result);
        verify(medicalVisitRepository, times(1)).findByPatientId(patientId);
        verify(mapperUtil, times(1)).mapList(visits, CreateMedicalVisitDTO.class);
    }

    @Test
    void testGetVisitHistoryByPatient_WhenNoVisitsFound() {
        Long patientId = 1L;

        when(medicalVisitRepository.findByPatientId(patientId)).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class, () -> medicalVisitService.getVisitHistoryByPatient(patientId),
            "No medical visits found for the specific patient");

        verify(medicalVisitRepository, times(1)).findByPatientId(patientId);
        verify(mapperUtil, never()).mapList(anyList(), eq(CreateMedicalVisitDTO.class));
    }

    @Test
    void testGetVisitHistoryForAllDoctors_Successful() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        MedicalVisit medicalVisit2 = mock();

        List<MedicalVisit> medicalVisits = List.of(medicalVisit, medicalVisit2);
        CreateMedicalVisitDTO createMedicalVisitDTO2 = mock();

        List<CreateMedicalVisitDTO> medicalVisitDTOs = List.of(createMedicalVisitDTO, createMedicalVisitDTO2);

        when(medicalVisitRepository.findByVisitDateBetween(startDate, endDate)).thenReturn(medicalVisits);
        when(mapperUtil.mapList(medicalVisits, CreateMedicalVisitDTO.class)).thenReturn(medicalVisitDTOs);

        List<CreateMedicalVisitDTO> result = medicalVisitService.getVisitHistoryForAllDoctors(startDate, endDate);

        assertEquals("Result should be as expected", medicalVisitDTOs, result);
        verify(medicalVisitRepository, times(1)).findByVisitDateBetween(startDate, endDate);
        verify(mapperUtil, times(1)).mapList(medicalVisits, CreateMedicalVisitDTO.class);
    }

    @Test
    void testGetVisitHistoryForAllDoctors_WhenNoVisitsFound() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(medicalVisitRepository.findByVisitDateBetween(startDate, endDate)).thenReturn(List.of());

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.getVisitHistoryForAllDoctors(startDate, endDate),
            "No medical visits for all doctors found");

        verify(medicalVisitRepository, times(1)).findByVisitDateBetween(startDate, endDate);
        verify(mapperUtil, never()).mapList(anyList(), eq(CreateMedicalVisitDTO.class));
    }

    @Test
    void testGetVisitHistoryForAllDoctors_WhenStartDateAfterEndDate() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(7);

        Assertions.assertThrows(
            InvalidDateRangeException.class, () -> medicalVisitService.getVisitHistoryForAllDoctors(startDate, endDate),
            "Start date cannot be after end date");

        verify(medicalVisitRepository, never()).findByVisitDateBetween(any(), any());
        verify(mapperUtil, never()).mapList(anyList(), eq(CreateMedicalVisitDTO.class));
    }

    @Test
    void testGetVisitHistoryForDoctor_Successful() {
        Long doctorId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        MedicalVisit medicalVisit2 = mock();
        CreateMedicalVisitDTO createMedicalVisitDTO2 = mock();

        List<MedicalVisit> visits = List.of(medicalVisit, medicalVisit2);

        List<CreateMedicalVisitDTO> medicalVisitDTO = List.of(createMedicalVisitDTO, createMedicalVisitDTO2);

        when(medicalVisitRepository.findByDoctorIdAndVisitDateBetween(doctorId, startDate, endDate)).thenReturn(visits);
        when(mapperUtil.mapList(visits, CreateMedicalVisitDTO.class)).thenReturn(medicalVisitDTO);

        List<CreateMedicalVisitDTO> result = medicalVisitService.getVisitHistoryForDoctor(doctorId, startDate, endDate);

        assertEquals("Result should be as expected", medicalVisitDTO, result);
        verify(medicalVisitRepository, times(1)).findByDoctorIdAndVisitDateBetween(doctorId, startDate, endDate);
        verify(mapperUtil, times(1)).mapList(visits, CreateMedicalVisitDTO.class);
    }

    @Test
    void testGetVisitHistoryForDoctor_WhenNoVisitsFound() {
        Long doctorId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(medicalVisitRepository.findByDoctorIdAndVisitDateBetween(doctorId, startDate, endDate)).thenReturn(
            List.of());

        assertThrows(ObjectNotFoundException.class,
            () -> medicalVisitService.getVisitHistoryForDoctor(doctorId, startDate, endDate),
            "No medical visits found for specific doctor within the given period");
        verify(medicalVisitRepository, times(1)).findByDoctorIdAndVisitDateBetween(doctorId, startDate, endDate);
        verify(mapperUtil, never()).mapList(anyList(), eq(CreateMedicalVisitDTO.class));
    }

    @Test
    void testGetVisitHistoryForDoctor_WhenStartDateAfterEndDate() {
        Long doctorId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(7);

        assertThrows(InvalidDateRangeException.class,
            () -> medicalVisitService.getVisitHistoryForDoctor(doctorId, startDate, endDate),
            "Start date cannot be after end date");

        verify(medicalVisitRepository, never()).findByDoctorIdAndVisitDateBetween(anyLong(), any(), any());
        verify(mapperUtil, never()).mapList(anyList(), eq(CreateMedicalVisitDTO.class));
    }
}
