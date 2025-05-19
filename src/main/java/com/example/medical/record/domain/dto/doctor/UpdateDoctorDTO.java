package com.example.medical.record.domain.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UpdateDoctorDTO {
    @NotBlank(message = "Doctor name cannot be blank")
    @Size(max = 100, message = "Doctor name cannot exceed 100 characters")
    private String doctorName;

    @NotNull(message = "GP status cannot be null")
    private Boolean isGp;
}
