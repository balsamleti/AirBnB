package com.airbnb.be.config;

import com.airbnb.be.api.ApiError;
import io.github.cdimascio.openapi.Validate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.cdimascio.openapi.Validate.Instance;

@Configuration
public class OpenApiValidate {

    private static final String OPEN_API_PATH = "static/api.yaml";

    @Bean
    public Validate<ApiError> validate() {
        return Instance.configure(OPEN_API_PATH, ApiError::new);
    }

}
