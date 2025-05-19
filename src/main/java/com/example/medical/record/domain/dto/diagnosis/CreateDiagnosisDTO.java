package com.example.medical.record.domain.dto.diagnosis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateDiagnosisDTO {
    @NotBlank(message = "Diagnosis name cannot be blank")
    @Size(max = 100, message = "Diagnosis name cannot exceed 100 characters")
    private String diagnosisName;
}
