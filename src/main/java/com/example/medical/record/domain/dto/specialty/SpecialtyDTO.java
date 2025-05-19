package com.example.medical.record.domain.dto.specialty;

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
public class SpecialtyDTO {
    private Long id;

    @NotBlank(message = "Specialty name cannot be blank")
    @Size(max = 60, message = "Specialty name cannot exceed 60 characters")
    private String specialtyName;
}
