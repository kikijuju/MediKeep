package com.medikeep.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseDto<T> {
    private final int status;
    private final String message;
    private final T data;

    public ApiResponseDto(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }
}
