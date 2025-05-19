package com.example.medical.record.domain.dto.visit;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MedicalVisitDTO {

    private Long medicalVisitId;

    @NotNull(message = "Visit date cannot be null")
    @FutureOrPresent(message = "Visit date cannot be in the past")
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @NotNull(message = "Patient ID cannot be null")
    private Long patientId;

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;

}
