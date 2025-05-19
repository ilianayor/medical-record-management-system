package com.example.medical.record.domain.dto.sickleave;

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
public class MonthWithHighestSickLeaveDTO {
    private int year;
    private int month;
    private long sickLeaveCount;
}
