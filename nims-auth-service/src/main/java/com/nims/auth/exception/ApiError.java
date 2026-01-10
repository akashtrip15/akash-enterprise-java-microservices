package com.nims.auth.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiError {
    private int status;
    private String error;
    private String code;
    private int numericCode;
    private String message;
    private String path;
    private Instant timestamp;
}
