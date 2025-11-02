package com.example.pets.infrastructure.web;

import com.example.pets.application.port.in.PetUseCase;
import com.example.pets.domain.model.Pet;
import com.example.pets.infrastructure.web.dto.PetCreateRequest;
import com.example.pets.infrastructure.web.dto.PetResponse;
import com.example.pets.infrastructure.web.dto.PetUpdateRequest;
import com.example.pets.infrastructure.web.mapper.PetMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
@Validated
public class PetController {

    private final PetUseCase petUseCase;
    private final PetMapper petMapper;

    @Operation(summary = "Create a new pet", description = "Creates a pet and returns the created pet details")
    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetCreateRequest request) {
        log.info("Creating pet: {}", request);
        Pet pet = petMapper.toDomain(request);
        Pet created = petUseCase.createPet(pet);
        PetResponse response = petMapper.toResponse(created);
        log.info("Created pet with ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a pet", description = "Updates an existing pet by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @Valid @RequestBody PetUpdateRequest request) {
        log.info("Updating pet with ID {}: {}", id, request);
        Pet petUpdates = petMapper.toDomain(request);
        Pet updated = petUseCase.updatePet(id, petUpdates);
        PetResponse response = petMapper.toResponse(updated);
        log.info("Updated pet with ID {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a pet by ID", description = "Returns the details of a pet by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> getPet(@PathVariable Long id) {
        log.info("Fetching pet with ID {}", id);
        Pet pet = petUseCase.getPet(id);
        PetResponse response = petMapper.toResponse(pet);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a pet", description = "Deletes a pet by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        log.info("Deleting pet with ID {}", id);
        petUseCase.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all pets", description = "Returns a list of all pets")
    @GetMapping
    public ResponseEntity<List<PetResponse>> getAllPets() {
        log.info("Fetching all pets");
        List<Pet> pets = petUseCase.getAllPets();
        List<PetResponse> responses = pets.stream()
                .map(petMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
