package com.example.pets.application.port.in;

import com.example.pets.domain.model.Pet;

import java.util.List;

public interface PetUseCase {

    Pet createPet(Pet pet);

    Pet updatePet(Long id, Pet pet);

    Pet getPet(Long id);

    void deletePet(Long id);

    List<Pet> getAllPets();
}


