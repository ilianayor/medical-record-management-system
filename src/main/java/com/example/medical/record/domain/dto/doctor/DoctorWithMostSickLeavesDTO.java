package com.example.medical.record.domain.dto.doctor;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class DoctorWithMostSickLeavesDTO {
    private Long doctorId;
    private String doctorName;
    private Long sickLeaveCount;
}
