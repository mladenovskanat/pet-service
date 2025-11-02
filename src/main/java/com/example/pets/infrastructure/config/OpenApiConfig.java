package com.example.pets.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Pet Service API",
        version = "v1",
        description = "CRUD API for pets"
))
public class OpenApiConfig {
}
