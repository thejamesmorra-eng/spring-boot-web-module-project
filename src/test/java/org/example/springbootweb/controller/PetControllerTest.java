package org.example.springbootweb.controller;

import org.example.springbootweb.exceptionHandler.exceptions.UserNotFoundException;
import org.example.springbootweb.model.PetDto;
import org.example.springbootweb.service.PetService;
import org.example.springbootweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreatePetWhenRequestIsValid() throws Exception {
        Long userId = 1L;

        PetDto petDto = new PetDto(null, "Buddy", null);
        PetDto savedPet = new PetDto(10L, "Buddy", userId);

        String petDtoJson = objectMapper.writeValueAsString(petDto);

        when(petService.createPet(eq(userId), any(PetDto.class))).thenReturn(savedPet);

        String createdPetDto = mockMvc.perform(post("/api/users/{userId}/pets", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(petDtoJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDto petDtoResponse = objectMapper.readValue(createdPetDto, PetDto.class);

        org.junit.jupiter.api.Assertions.assertEquals(10L, petDtoResponse.getId());
        org.junit.jupiter.api.Assertions.assertEquals(petDto.getName(), petDtoResponse.getName());
        org.junit.jupiter.api.Assertions.assertEquals(userId, petDtoResponse.getUserId());
    }

    @Test
    void shouldErrorCreatePetWhenRequestIsNotValid() throws Exception {
        PetDto petDto = new PetDto(null, "",null);
        String petDtoJson = objectMapper.writeValueAsString(petDto);

        mockMvc.perform(post("/api/users/{userId}/pets", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(petDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldErrorCreatePetWhenUserIdIsNotExist() throws Exception {
        Long notExistentId = 999L;
        PetDto petDto = new PetDto(null, "Buddy", null);
        String petDtoJson = objectMapper.writeValueAsString(petDto);

        when(userService.getUserById(notExistentId))
                .thenThrow(new UserNotFoundException("User not found with id: " + notExistentId));

        mockMvc.perform(post("/api/users/{userId}/pets", notExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(petDtoJson))
                .andExpect(status().isNotFound());
    }
}