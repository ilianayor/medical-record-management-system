package com.example.medical.record.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "medical_visit")
@Getter
@Setter
public class MedicalVisit extends BaseEntity {

    @NotNull(message = "Visit date cannot be null")
    @FutureOrPresent(message = "Visit date cannot be in the past")
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Patient ID cannot be null")
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Doctor ID cannot be null")
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToMany(mappedBy = "medicalVisit")
    @JsonIgnore
    private Set<Treatment> treatments;

    @OneToOne(mappedBy = "medicalVisit")
    @JsonIgnore
    private SickLeave sickLeave;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "medical_visit_has_diagnosis",
        joinColumns = @JoinColumn(name = "medical_visit_id"),
        inverseJoinColumns = @JoinColumn(name = "diagnosis_id")
    )
    @JsonIgnore
    private Set<Diagnosis> diagnoses;

}
