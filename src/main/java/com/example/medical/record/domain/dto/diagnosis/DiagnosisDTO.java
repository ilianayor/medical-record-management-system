package com.example.medical.record.domain.dto.diagnosis;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
public class DiagnosisDTO {

    private Long diagnosisId;
    @NotBlank(message = "Diagnosis name cannot be blank")
    @Size(max = 100, message = "Diagnosis name cannot exceed 100 characters")
    private String diagnosisName;
}
