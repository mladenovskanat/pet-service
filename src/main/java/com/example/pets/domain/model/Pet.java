package com.example.pets.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Pet {

    private final Long id;
    private final String name;
    private final String species;
    private final Integer age;
    private final String ownerName;

    public Pet(Long id, String name, String species, Integer age, String ownerName) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (species == null || species.isBlank()) {
            throw new IllegalArgumentException("Species is required");
        }
        if (age != null && age < 0) {
            throw new IllegalArgumentException("Age must be >= 0");
        }

        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
    }

    public Pet rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return new Pet(this.id, newName, this.species, this.age, this.ownerName);
    }

    public Pet changeSpecies(String newSpecies) {
        if (newSpecies == null || newSpecies.isBlank()) {
            throw new IllegalArgumentException("Species cannot be empty");
        }
        return new Pet(this.id, this.name, newSpecies, this.age, this.ownerName);
    }

    public Pet updateAge(Integer newAge) {
        if (newAge != null && newAge < 0) {
            throw new IllegalArgumentException("Age must be >= 0");
        }
        return new Pet(this.id, this.name, this.species, newAge, this.ownerName);
    }

    public Pet changeOwner(String newOwner) {
        return new Pet(this.id, this.name, this.species, this.age, newOwner);
    }
}


