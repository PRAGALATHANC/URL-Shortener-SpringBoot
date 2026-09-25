package com.stackfortech.urlshortening.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, LocalDateTime timestamp) {
    public static ErrorResponse of(int status, String error) {
        return new ErrorResponse(status, error, LocalDateTime.now());
    }
}
