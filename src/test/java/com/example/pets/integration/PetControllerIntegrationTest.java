package com.example.pets.integration;

import com.example.pets.infrastructure.web.dto.PetCreateRequest;
import com.example.pets.infrastructure.web.dto.PetResponse;
import com.example.pets.infrastructure.web.dto.PetUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("inmemory")
class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPet_returnsCreatedPet() throws Exception {
        PetCreateRequest request = new PetCreateRequest("Bini", "Cat", 2, "Natasha");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        PetResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PetResponse.class);
        assertThat(response.name()).isEqualTo("Bini");
        assertThat(response.ownerName()).isEqualTo("Natasha");
    }

    @Test
    void getPet_existingPet_returnsPet() throws Exception {
        PetCreateRequest createRequest = new PetCreateRequest("Bini", "Cat", 2, "Natasha");
        MvcResult postResult = mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        PetResponse createdPet = objectMapper.readValue(postResult.getResponse().getContentAsString(), PetResponse.class);
        Long petId = createdPet.id();

        MvcResult getResult = mockMvc.perform(get("/api/v1/pets/" + petId))
                .andExpect(status().isOk())
                .andReturn();

        PetResponse response = objectMapper.readValue(getResult.getResponse().getContentAsString(), PetResponse.class);

        assertThat(response.id()).isEqualTo(petId);
        assertThat(response.name()).isEqualTo("Bini");
        assertThat(response.ownerName()).isEqualTo("Natasha");
    }

    @Test
    void updatePet_existingPet_returnsUpdatedPet() throws Exception {
        PetCreateRequest createRequest = new PetCreateRequest("Bini", "Cat", 2, "Natasha");
        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        PetUpdateRequest updateRequest = new PetUpdateRequest("BiniUpdated", "Cat", 3, "NatashaUpdated");
        MvcResult mvcResult = mockMvc.perform(put("/api/v1/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        PetResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PetResponse.class);
        assertThat(response.name()).isEqualTo("BiniUpdated");
        assertThat(response.ownerName()).isEqualTo("NatashaUpdated");
    }

    @Test
    void deletePet_existingPet_returnsNoContent() throws Exception {
        PetCreateRequest createRequest = new PetCreateRequest("Bini", "Cat", 2, "Natasha");
        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/v1/pets/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/pets/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllPets_returnsAllCreatedPets() throws Exception {
        PetCreateRequest pet1 = new PetCreateRequest("Bini", "Cat", 2, "Natasha");
        PetCreateRequest pet2 = new PetCreateRequest("Fido", "Dog", 3, "John");

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pet2)))
                .andExpect(status().isCreated());

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/pets"))
                .andExpect(status().isOk())
                .andReturn();

        PetResponse[] responses = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), PetResponse[].class);
        assertThat(responses).hasSize(2);
    }
}
