package com.airbnb.be.api;

import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {

    @Getter
    private final ApiError apiError;

    public ApiException(ApiError apiError) {
        super(apiError.getStatus(), apiError.getMessage());
        this.apiError = apiError;
    }

}