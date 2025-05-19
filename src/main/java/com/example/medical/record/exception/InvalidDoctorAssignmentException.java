package com.example.medical.record.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDoctorAssignmentException extends InvalidValidationException {
    private final Long doctorId;

    public InvalidDoctorAssignmentException(String message, Long doctorId) {
        super(message);
        this.doctorId = doctorId;
    }
}
