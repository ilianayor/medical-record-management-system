package com.example.medical.record.domain.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdatePatientDTO {
    @NotBlank(message = "UCN cannot be blank")
    @Pattern(regexp = "\\d{10}", message = "UCN must be exactly 10 digits")
    @Size(min = 10, max = 10, message = "UCN must be 10 characters")
    private String ucn;

    @NotBlank(message = "Patient name cannot be blank")
    @Size(max = 100, message = "Patient name cannot exceed 100 characters")
    private String patientName;

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;
}
