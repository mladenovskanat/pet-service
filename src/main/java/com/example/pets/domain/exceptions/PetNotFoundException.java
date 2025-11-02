package com.example.pets.domain.exceptions;

import lombok.Getter;

@Getter
public class PetNotFoundException extends RuntimeException {
    private final Long petId;

    public PetNotFoundException(Long petId) {
        super("Pet not found: " + petId);
        this.petId = petId;
    }
}



