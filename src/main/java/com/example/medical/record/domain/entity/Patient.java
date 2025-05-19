package com.example.medical.record.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "patient")
@Getter
@Setter
public class Patient extends BaseEntity {

    @NotBlank(message = "UCN cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "UCN must be exactly 10 digits")
    @Column(name = "patient_ucn", unique = true, nullable = false)
    private String ucn;

    @NotBlank(message = "Patient name cannot be blank")
    @Size(max = 100, message = "Patient name cannot exceed 100 characters")
    @Column(name = "patient_name", nullable = false)
    private String patientName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private Set<InsurancePayment> healthInsurancePayments;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    private Set<MedicalVisit> medicalVisits;

    @OneToOne
    private User user;

}
