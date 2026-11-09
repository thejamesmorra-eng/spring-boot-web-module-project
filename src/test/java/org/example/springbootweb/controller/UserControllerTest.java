package org.example.springbootweb.controller;

import org.assertj.core.api.Assertions;
import org.example.springbootweb.exceptionHandler.exceptions.UserNotFoundException;
import org.example.springbootweb.model.UserDto;
import org.example.springbootweb.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateUserWhenRequestIsValid() throws Exception {
        UserDto userDto = new UserDto(null, "Ivan", "email@email.ru", 27, null);
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        UserDto savedUser = new UserDto(1L, "Ivan", "email@email.ru", 27, null);
        when(userService.createUser(any(UserDto.class))).thenReturn(savedUser);

        String createdUserDto = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson)).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto userDtoResponse = objectMapper.readValue(createdUserDto, UserDto.class);

        org.junit.jupiter.api.Assertions.assertEquals(1L, userDtoResponse.getId());
        org.junit.jupiter.api.Assertions.assertEquals(userDto.getName(), userDtoResponse.getName());
        org.junit.jupiter.api.Assertions.assertEquals(userDto.getEmail(), userDtoResponse.getEmail());
        org.junit.jupiter.api.Assertions.assertEquals(userDto.getAge(), userDtoResponse.getAge());
        org.junit.jupiter.api.Assertions.assertEquals(userDto.getPets(), userDtoResponse.getPets());
    }

    @Test
    void shouldErrorCreateUserWhenRequestIsNotValid() throws Exception {
        UserDto userDto = new UserDto(null, "", "email@email.ru", 27, null);
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSuccessGetRequestWhenIdIsExist() throws Exception {
        Long userId = 1L;
        UserDto savedUser = new UserDto(userId, "Ivan", "email@email.ru", 27, null);

        when(userService.getUserById(userId)).thenReturn(savedUser);

        String foundUserDtoJson = mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDto foundedUserDto = objectMapper.readValue(foundUserDtoJson, UserDto.class);

        Assertions.assertThat(savedUser)
                .usingRecursiveComparison()
                .isEqualTo(foundedUserDto);
    }

    @Test
    void shouldErrorGetRequestWhenIdIsNotExist() throws Exception {
        Long nonExistentId = 999L;

        when(userService.getUserById(nonExistentId))
                .thenThrow(new UserNotFoundException("User not found with id: " + nonExistentId));

        mockMvc.perform(get("/api/users/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}