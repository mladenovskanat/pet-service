package com.example.pets.infrastructure.persistence.inmemory;

import com.example.pets.domain.model.Pet;
import com.example.pets.domain.ports.PetRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
@Profile("inmemory")
public class InMemoryPetRepositoryAdapter implements PetRepository {

    private final Map<Long, PetEntity> database = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    private final PetEntityMapper mapper;

    public InMemoryPetRepositoryAdapter(PetEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Pet save(Pet pet) {
        Long id = Optional.ofNullable(pet.getId()).orElseGet(idGenerator::incrementAndGet);
        PetEntity entity = mapper.toEntity(pet);
        entity.setId(id);
        database.put(id, entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return Optional.ofNullable(database.get(id)).map(mapper::toDomain);
    }

    @Override
    public List<Pet> findAll() {
        return database.values().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return database.containsKey(id);
    }
}




