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
public class CreateDoctorDTO {
    @NotBlank(message = "Doctor name cannot be blank")
    @Size(max = 100, message = "Doctor name cannot exceed 100 characters")
    private String doctorName;

    @NotNull(message = "GP status cannot be null")
    private Boolean isGp;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
