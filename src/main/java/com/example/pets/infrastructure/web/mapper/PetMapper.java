package com.example.pets.infrastructure.web.mapper;

import com.example.pets.domain.model.Pet;
import com.example.pets.infrastructure.web.dto.PetCreateRequest;
import com.example.pets.infrastructure.web.dto.PetUpdateRequest;
import com.example.pets.infrastructure.web.dto.PetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "id", ignore = true)
    Pet toDomain(PetCreateRequest request);

    Pet toDomain(PetUpdateRequest request);

    PetResponse toResponse(Pet pet);
}


