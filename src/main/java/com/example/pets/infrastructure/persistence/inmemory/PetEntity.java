package com.example.pets.infrastructure.persistence.inmemory;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetEntity {

    private Long id; //ID managed manually since this app uses in-memory database
    private String name;
    private String species;
    private Integer age;
    private String ownerName;
}

