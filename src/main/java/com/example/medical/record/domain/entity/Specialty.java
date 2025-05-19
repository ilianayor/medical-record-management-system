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
@Table(name = "specialty")
@Getter
@Setter
public class Specialty extends BaseEntity {
    @NotBlank(message = "Specialty name cannot be blank")
    @Size(max = 60, message = "Specialty name cannot exceed 60 characters")
    @Column(name = "specialty_name", nullable = false)
    private String specialtyName;

    @ManyToMany(mappedBy = "specialties")
    @JsonIgnore
    private Set<Doctor> doctors;
}
