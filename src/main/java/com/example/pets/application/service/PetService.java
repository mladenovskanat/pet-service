package com.example.pets.application.service;

import com.example.pets.application.port.in.PetUseCase;
import com.example.pets.domain.exceptions.PetNotFoundException;
import com.example.pets.domain.model.Pet;
import com.example.pets.domain.ports.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService implements PetUseCase {

    private final PetRepository petRepository;

    @Override
    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet updatePet(Long id, Pet petUpdates) {
        Pet existing = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        Pet updated = applyUpdates(existing, petUpdates);
        return petRepository.save(updated);
    }

    @Override
    public Pet getPet(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }

    @Override
    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new PetNotFoundException(id);
        }
        petRepository.deleteById(id);
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    private Pet applyUpdates(Pet pet, Pet updates) {
        if (updates.getName() != null) pet = pet.rename(updates.getName());
        if (updates.getSpecies() != null) pet = pet.changeSpecies(updates.getSpecies());
        if (updates.getAge() != null) pet = pet.updateAge(updates.getAge());
        if (updates.getOwnerName() != null) pet = pet.changeOwner(updates.getOwnerName());
        return pet;
    }
}
