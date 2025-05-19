package com.example.medical.record.service;

import com.example.medical.record.domain.dto.visit.CreateMedicalVisitDTO;
import com.example.medical.record.domain.dto.visit.MedicalVisitDTO;
import com.example.medical.record.domain.entity.MedicalVisit;
import com.example.medical.record.repository.MedicalVisitRepository;
import com.example.medical.record.service.impl.MedicalVisitServiceImpl;
import com.example.medical.record.util.MapperUtil;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Configuration
public class MedicalVisitServiceImplMethodLevelSecurityTest {
    @MockitoBean
    private MedicalVisitRepository medicalVisitRepository;

    @MockitoBean
    private MapperUtil mapperUtil;

    @Autowired
    private MedicalVisitServiceImpl medicalVisitService;

    @Mock
    MedicalVisit medicalVisit;

    private final Long medicalVisitId = 1L;

    private final Long diagnosisId = 1L;

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testGetMedicalVisitById_ShouldFailWithPatient() {
        assertThrows(AuthorizationDeniedException.class, () -> medicalVisitService.getMedicalVisitById(medicalVisitId));
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testGetMedicalVisitById_ShouldSucceedWithDoctor() {
        MedicalVisit medicalVisit = mock();
        when(medicalVisit.getId()).thenReturn(medicalVisitId);

        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.of(medicalVisit));
        when(mapperUtil.getModelMapper()).thenReturn(new ModelMapper());

        assertDoesNotThrow(() -> {
            MedicalVisitDTO result = medicalVisitService.getMedicalVisitById(medicalVisitId);
            assertNotNull(result);
            assertEquals(medicalVisitId, result.getMedicalVisitId());
        }, "Security check should pass");

        verify(medicalVisitRepository).findById(medicalVisitId);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetMedicalVisitById_ShouldSucceedWithAdmin() {
        when(medicalVisit.getId()).thenReturn(medicalVisitId);

        when(medicalVisitRepository.findById(medicalVisitId)).thenReturn(Optional.of(medicalVisit));
        when(mapperUtil.getModelMapper()).thenReturn(new ModelMapper());

        assertDoesNotThrow(() -> {
            MedicalVisitDTO result = medicalVisitService.getMedicalVisitById(medicalVisitId);
            assertNotNull(result);
            assertEquals(medicalVisitId, result.getMedicalVisitId());
        }, "Security check should pass");

        verify(medicalVisitRepository).findById(medicalVisitId);
    }

    @Test
    @WithAnonymousUser
    void getMedicalVisitById_ShouldFailWithAnonymous() {
        assertThrows(AuthorizationDeniedException.class, () -> medicalVisitService.getMedicalVisitById(medicalVisitId));
    }

    @Test
    void getMedicalVisitById_ShouldFailWithoutUser() {
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> medicalVisitService.getMedicalVisitById(medicalVisitId));
    }


    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testAddDiagnosisToMedicalVisit_ShouldFailWithPatient() {
        assertThrows(AuthorizationDeniedException.class, () -> medicalVisitService.addDiagnosisToMedicalVisit(medicalVisitId, diagnosisId));
        verify(medicalVisitRepository, never()).save(any(MedicalVisit.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetVisitHistoryByPatient_ShouldSucceedWithAdmin() {
        List<MedicalVisit> medicalVisits = List.of(medicalVisit);

        CreateMedicalVisitDTO dto1 = mock();
        List<CreateMedicalVisitDTO> visitDTOs = List.of(dto1);

       when(medicalVisitRepository.findByPatientId(1L)).thenReturn(medicalVisits);
       when(mapperUtil.mapList(medicalVisits, CreateMedicalVisitDTO.class)).thenReturn(visitDTOs);

        assertDoesNotThrow(() -> medicalVisitService.getVisitHistoryByPatient(1L));

        List<CreateMedicalVisitDTO> result = medicalVisitService.getVisitHistoryByPatient(1L);
        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser(authorities = {"DOCTOR"})
    void testGetVisitHistoryByPatient_ShouldSucceedWithDoctor() {
        List<MedicalVisit> medicalVisits = List.of(medicalVisit);

        CreateMedicalVisitDTO dto1 = mock();
        List<CreateMedicalVisitDTO> visitDTOs = List.of(dto1);

        when(medicalVisitRepository.findByPatientId(1L)).thenReturn(medicalVisits);
        when(mapperUtil.mapList(medicalVisits, CreateMedicalVisitDTO.class)).thenReturn(visitDTOs);

        assertDoesNotThrow(() -> medicalVisitService.getVisitHistoryByPatient(1L));
        List<CreateMedicalVisitDTO> result = medicalVisitService.getVisitHistoryByPatient(1L);
        assertEquals(1, result.size());
    }

    @Test
    @WithMockUser(authorities = {"PATIENT"})
    void testGetVisitHistoryByPatient_PatientHasDifferentIdAccessDenied() {
        Authentication authentication = mock();

        when(authentication.getPrincipal()).thenReturn(new PatientPrincipal(2L));
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(AuthorizationDeniedException.class, () -> medicalVisitService.getVisitHistoryByPatient(1L));
    }

    @Getter
    private static class PatientPrincipal {
        private final Long patientId;
        public PatientPrincipal(Long patientId) {
            this.patientId = patientId;
        }
    }
}
