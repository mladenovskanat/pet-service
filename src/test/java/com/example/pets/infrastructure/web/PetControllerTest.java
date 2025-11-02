package com.example.pets.infrastructure.web;

import com.example.pets.application.port.in.PetUseCase;
import com.example.pets.domain.model.Pet;
import com.example.pets.infrastructure.web.dto.PetCreateRequest;
import com.example.pets.infrastructure.web.dto.PetResponse;
import com.example.pets.infrastructure.web.dto.PetUpdateRequest;
import com.example.pets.infrastructure.web.mapper.PetMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PetControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PetUseCase petUseCase;

    @Mock
    private PetMapper petMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PetController controller = new PetController(petUseCase, petMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createPet_validRequest_returnsCreatedPet() throws Exception {
        //arrange
        PetCreateRequest request = new PetCreateRequest("Toto", "Dog", 3, "Natasha");
        Pet domainPet = new Pet(1L, "Toto", "Dog", 3, "Natasha");
        PetResponse response = new PetResponse(1L, "Toto", "Dog", 3, "Natasha");

        when(petMapper.toDomain(request)).thenReturn(domainPet);
        when(petUseCase.createPet(any(Pet.class))).thenReturn(domainPet);
        when(petMapper.toResponse(domainPet)).thenReturn(response);

        //act
        var result = mockMvc.perform(post("/api/v1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Toto"))
                .andExpect(jsonPath("$.ownerName").value("Natasha"));
    }

    @Test
    void updatePet_validRequest_returnsUpdatedPet() throws Exception {
        //arrange
        PetUpdateRequest request = new PetUpdateRequest("Koki", null, 4, null);
        Pet domainPet = new Pet(1L, "Koki", "Dog", 4, "Natasha");
        PetResponse response = new PetResponse(1L, "Koki", "Dog", 4, "Natasha");

        when(petMapper.toDomain(request)).thenReturn(domainPet);
        when(petUseCase.updatePet(eq(1L), any(Pet.class))).thenReturn(domainPet);
        when(petMapper.toResponse(domainPet)).thenReturn(response);

        //act
        var result = mockMvc.perform(put("/api/v1/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Koki"))
                .andExpect(jsonPath("$.age").value(4))
                .andExpect(jsonPath("$.ownerName").value("Natasha"));
    }

    @Test
    void getPet_existingId_returnsPet() throws Exception {
        //arrange
        Pet domainPet = new Pet(1L, "Toto", "Dog", 3, "Natasha");
        PetResponse response = new PetResponse(1L, "Toto", "Dog", 3, "Natasha");

        when(petUseCase.getPet(1L)).thenReturn(domainPet);
        when(petMapper.toResponse(domainPet)).thenReturn(response);

        //act
        var result = mockMvc.perform(get("/api/v1/pets/1"));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Toto"))
                .andExpect(jsonPath("$.ownerName").value("Natasha"));
    }

    @Test
    void deletePet_existingId_returnsNoContent() throws Exception {
        //arrange
        doNothing().when(petUseCase).deletePet(1L);

        //act
        var result = mockMvc.perform(delete("/api/v1/pets/1"));

        //assert
        result.andExpect(status().isNoContent());
        verify(petUseCase, times(1)).deletePet(1L);
    }

    @Test
    void getAllPets_returnsListOfPets() throws Exception {
        //arrange
        Pet pet1 = new Pet(1L, "Toto", "Dog", 3, "Natasha");
        Pet pet2 = new Pet(2L, "Bini", "Cat", 2, "Natasha");
        PetResponse response1 = new PetResponse(1L, "Toto", "Dog", 3, "Natasha");
        PetResponse response2 = new PetResponse(2L, "Bini", "Cat", 2, "Natasha");

        when(petUseCase.getAllPets()).thenReturn(List.of(pet1, pet2));
        when(petMapper.toResponse(pet1)).thenReturn(response1);
        when(petMapper.toResponse(pet2)).thenReturn(response2);

        //act
        var result = mockMvc.perform(get("/api/v1/pets"));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Toto"))
                .andExpect(jsonPath("$[1].name").value("Bini"));
    }
}
