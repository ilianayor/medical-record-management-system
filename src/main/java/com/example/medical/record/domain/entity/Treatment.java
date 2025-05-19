package com.example.medical.record.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "treatment")
@Getter
@Setter
public class Treatment extends BaseEntity {

    @NotBlank(message = "Treatment type cannot be blank")
    @Size(max = 45, message = "Treatment type cannot exceed 45 characters")
    @Column(nullable = false)
    private String treatmentType;

    @NotBlank(message = "Treatment description cannot be blank")
    @Size(max = 100, message = "Treatment description cannot exceed 100 characters")
    @Column(nullable = false)
    private String treatmentDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_visit_id", nullable = false)
    private MedicalVisit medicalVisit;
}
