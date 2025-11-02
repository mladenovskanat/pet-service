package com.example.pets.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;

public record PetCreateRequest(

        @NotBlank
        @Pattern(regexp = "^[A-Za-z]+$", message = "Name must contain only letters")
        @Schema(description = "Name of the pet", example = "Bini")
        String name,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z]+$", message = "Species must contain only letters")
        @Schema(description = "Species of the pet", example = "Cat")
        String species,

        @Min(value = 0, message = "Age must be zero or positive")
        @Schema(description = "Age of the pet", example = "1")
        Integer age,

        @Pattern(regexp = "^[A-Za-z]+$", message = "Owner name must contain only letters")
        @Schema(description = "Owner's name", example = "Natasha")
        String ownerName
) {
}



