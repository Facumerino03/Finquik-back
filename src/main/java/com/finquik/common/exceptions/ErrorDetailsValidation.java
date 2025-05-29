package com.finquik.common.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErrorDetailsValidation extends ErrorDetails {
    private Map<String, String> fieldErrors;

    public ErrorDetailsValidation(LocalDateTime timestamp, String message, String path, int status, Map<String, String> fieldErrors) {
        super(timestamp, message, path, status);
        this.fieldErrors = fieldErrors;
    }
}