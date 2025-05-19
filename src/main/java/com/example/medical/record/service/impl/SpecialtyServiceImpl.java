package com.example.medical.record.service.impl;

import com.example.medical.record.domain.dto.specialty.CreateSpecialtyDTO;
import com.example.medical.record.domain.dto.specialty.SpecialtyDTO;
import com.example.medical.record.domain.entity.Specialty;
import com.example.medical.record.exception.ObjectAlreadyExistsException;
import com.example.medical.record.exception.ObjectNotFoundException;
import com.example.medical.record.repository.SpecialtyRepository;
import com.example.medical.record.service.SpecialtyService;
import com.example.medical.record.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    private final MapperUtil mapperUtil;

    @Override
    public SpecialtyDTO getSpecialtyById(Long specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
            .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Specialty with id [%s] not found", specialtyId)));

        return mapperUtil.getModelMapper().map(specialty, SpecialtyDTO.class);
    }

    @Override
    public List<SpecialtyDTO> getAllSpecialties() {
        List<Specialty> specialties = specialtyRepository.findAll();

        if (specialties.isEmpty()) {
            throw new ObjectNotFoundException("Specialties not found.");
        }

        return mapperUtil.mapList(specialties, SpecialtyDTO.class);
    }

    @Override
    public ResponseEntity<SpecialtyDTO> createSpecialty(CreateSpecialtyDTO createSpecialtyDTO) {
        if (specialtyRepository.existsBySpecialtyName(createSpecialtyDTO.getSpecialtyName())) {
            throw new ObjectAlreadyExistsException(
                String.format("Specialty with name [%s] already exists", createSpecialtyDTO.getSpecialtyName()));
        }
        Specialty specialty = mapperUtil.getModelMapper().map(createSpecialtyDTO, Specialty.class);
        Specialty savedSpecialty = specialtyRepository.save(specialty);
        SpecialtyDTO specialtyDTO = mapperUtil.getModelMapper().map(savedSpecialty, SpecialtyDTO.class);

        return new ResponseEntity<>(specialtyDTO, HttpStatus.CREATED);
    }

    @Override
    public SpecialtyDTO updateSpecialty(Long specialtyId, CreateSpecialtyDTO createSpecialtyDTO) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
            .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Specialty with id [%s] not found", specialtyId)));

        mapperUtil.getModelMapper().map(createSpecialtyDTO, specialty);
        Specialty updatedSpecialty = specialtyRepository.save(specialty);
        return mapperUtil.getModelMapper().map(updatedSpecialty, SpecialtyDTO.class);
    }

    @Override
    public void deleteSpecialty(Long specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
            .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Specialty with id [%s] not found", specialtyId)));

        specialtyRepository.delete(specialty);
    }

    @Override
    public Specialty getSpecialty(Long specialtyId) {
        return specialtyRepository.findById(specialtyId)
            .orElseThrow(
                () -> new ObjectNotFoundException(String.format("Specialty with id [%s] not found", specialtyId)));
    }
}
