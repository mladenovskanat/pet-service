package com.example.pets.infrastructure.persistence.inmemory;

import com.example.pets.domain.model.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetEntityMapper {

    PetEntity toEntity(Pet pet);

    Pet toDomain(PetEntity entity);
}

