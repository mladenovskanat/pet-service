package com.example.pets.infrastructure.persistence.inmemory;

import com.example.pets.domain.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InMemoryPetRepositoryAdapterTest {

    @Mock
    private PetEntityMapper mapper;

    private InMemoryPetRepositoryAdapter repository;

    private Pet pet;
    private PetEntity entity;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPetRepositoryAdapter(mapper);
        pet = new Pet(null, "Toto", "Dog", 3, "Natasha");
        entity = new PetEntity(null, "Toto", "Dog", 3, "Natasha");
    }

    @Test
    void save_newPet_assignsIdAndReturnsDomainPet() {
        //arrange
        when(mapper.toEntity(pet)).thenReturn(entity);
        when(mapper.toDomain(any(PetEntity.class)))
                .thenAnswer(inv -> {
                    PetEntity e = inv.getArgument(0);
                    return new Pet(e.getId(), e.getName(), e.getSpecies(), e.getAge(), e.getOwnerName());
                });

        //act
        Pet saved = repository.save(pet);

        //assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Toto");
    }

    @Test
    void findById_existingPet_returnsOptionalWithPet() {
        //arrange
        when(mapper.toEntity(pet)).thenReturn(entity);
        when(mapper.toDomain(any(PetEntity.class)))
                .thenReturn(new Pet(1L, "Toto", "Dog", 3, "Natasha"));
        repository.save(pet);

        //act
        Optional<Pet> found = repository.findById(1L);

        //assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Toto");
    }

    @Test
    void deleteById_existingPet_removesFromRepository() {
        //arrange
        when(mapper.toEntity(pet)).thenReturn(entity);
        when(mapper.toDomain(any(PetEntity.class)))
                .thenReturn(new Pet(1L, "Toto", "Dog", 3, "Natasha"));
        Pet saved = repository.save(pet);

        //act
        repository.deleteById(saved.getId());

        //assert
        assertThat(repository.findAll()).isEmpty();
    }
}
