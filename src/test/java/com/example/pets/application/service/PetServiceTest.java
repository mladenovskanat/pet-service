package com.example.pets.application.service;

import com.example.pets.domain.exceptions.PetNotFoundException;
import com.example.pets.domain.model.Pet;
import com.example.pets.domain.ports.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    private Pet originalDog;
    private Pet updatedDog;
    private Pet originalCat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        originalDog = new Pet(1L, "Toto", "Dog", 3, "Natasha");
        updatedDog = new Pet(1L, "Koki", "Dog", 4, "Natasha");
        originalCat = new Pet(2L, "Bini", "Cat", 2, "Natasha");
    }

    @Test
    void createPet_validPet_returnsSavedPet() {
        //arrange
        when(petRepository.save(originalDog)).thenReturn(originalDog);

        //act
        Pet result = petService.createPet(originalDog);

        //assert
        assertThat(result).isEqualTo(originalDog);
        verify(petRepository).save(originalDog);
    }

    @Test
    void getPet_existingId_returnsPet() {
        //arrange
        when(petRepository.findById(1L)).thenReturn(Optional.of(originalDog));

        //act
        Pet result = petService.getPet(1L);

        //assert
        assertThat(result).isEqualTo(originalDog);
        verify(petRepository).findById(1L);
    }

    @Test
    void getPet_nonExistingId_throwsPetNotFoundException() {
        //arrange
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThatThrownBy(() -> petService.getPet(1L))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    void updatePet_existingPet_returnsUpdatedPet() {
        //arrange
        when(petRepository.findById(1L)).thenReturn(Optional.of(originalDog));
        when(petRepository.save(any(Pet.class))).thenReturn(updatedDog);

        //act
        Pet result = petService.updatePet(1L, updatedDog);

        //assert
        assertThat(result).isEqualTo(updatedDog);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void updatePet_nonExistingPet_throwsPetNotFoundException() {
        //arrange
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        //act & assert
        assertThatThrownBy(() -> petService.updatePet(1L, updatedDog))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    void deletePet_existingPet_callsDeleteById() {
        //arrange
        when(petRepository.existsById(1L)).thenReturn(true);

        //act
        petService.deletePet(1L);

        //assert
        verify(petRepository).deleteById(1L);
    }

    @Test
    void deletePet_nonExistingPet_throwsPetNotFoundException() {
        //arrange
        when(petRepository.existsById(1L)).thenReturn(false);

        //act & assert
        assertThatThrownBy(() -> petService.deletePet(1L))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    void getAllPets_existingPets_returnsListOfPets() {
        //arrange
        when(petRepository.findAll()).thenReturn(List.of(originalDog, originalCat));

        //act
        List<Pet> result = petService.getAllPets();

        //assert
        assertThat(result).containsExactly(originalDog, originalCat);
        verify(petRepository).findAll();
    }
}
