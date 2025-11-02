package com.example.pets.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PetResponse(
        @Schema(description = "ID of the pet", example = "1")
        Long id,

        @Schema(description = "Name of the pet", example = "Bini")
        String name,

        @Schema(description = "Species of the pet", example = "Cat")
        String species,

        @Schema(description = "Age of the pet", example = "2")
        Integer age,

        @Schema(description = "Owner's name", example = "Natasha")
        String ownerName
) {
}
