package com.example.medical.record.domain.dto.specialty;

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
public class CreateSpecialtyDTO {
    @NotBlank(message = "Specialty name cannot be blank")
    @Size(max = 60, message = "Specialty name cannot exceed 60 characters")
    private String specialtyName;
}
