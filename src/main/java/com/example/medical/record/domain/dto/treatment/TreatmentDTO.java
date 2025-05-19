package com.example.medical.record.domain.dto.treatment;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TreatmentDTO {
    private Long id;

    @NotBlank(message = "Treatment type cannot be blank")
    @Size(max = 45, message = "Treatment type cannot exceed 45 characters")
    @Column(nullable = false)
    private String treatmentType;

    @NotBlank(message = "Treatment description cannot be blank")
    @Size(max = 100, message = "Treatment description cannot exceed 100 characters")
    @Column(nullable = false)
    private String treatmentDescription;

    @NotNull(message = "Medical visit ID cannot be null")
    private Long medicalVisitId;
}
