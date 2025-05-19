package com.example.medical.record.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "diagnosis")
@Getter
@Setter
public class Diagnosis extends BaseEntity {

    @NotBlank(message = "Diagnosis name cannot be blank")
    @Size(max = 100, message = "Diagnosis name cannot exceed 100 characters")
    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosisName;

    @ManyToMany(mappedBy = "diagnoses")
    @JsonIgnore
    private Set<MedicalVisit> medicalVisits;
}

