package com.example.medical.record.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "doctor")
@Getter
@Setter
public class Doctor extends BaseEntity {

    @NotBlank(message = "Doctor name cannot be blank")
    @Size(max = 100, message = "Doctor name cannot exceed 100 characters")
    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "is_gp", nullable = false)
    private Boolean isGp;

    @ManyToMany
    @JoinTable(
        name = "doctor_has_specialty",
        joinColumns = @JoinColumn(name = "doctor_id"),
        inverseJoinColumns = @JoinColumn(name = "specialty_id")
    )
    @JsonIgnore
    private Set<Specialty> specialties;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Set<Patient> patients;

    @OneToMany(mappedBy = "doctor")
    @JsonIgnore
    private Set<MedicalVisit> medicalVisits;

    @OneToOne
    private User user;
}
