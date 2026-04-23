package org.example.springbootweb.service;

import org.example.springbootweb.exceptionHandler.exceptions.UserNotFoundException;
import org.example.springbootweb.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Long nextId;
    private final Map<Long, UserDto> users;

    public UserService() {
        nextId = 0L;
        this.users = new HashMap<>();
    }

    public UserDto createUser(UserDto userDto) {
        UserDto newUserDto = new UserDto(
                ++nextId,
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge(),
                new ArrayList<>());
        users.put(newUserDto.getId(), newUserDto);
        return newUserDto;
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        UserDto updatedUser = new UserDto(
                id,
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge(),
                userDto.getPets());
        users.put(id, updatedUser);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        Optional.ofNullable(users.remove(id))
                .orElseThrow(() -> new UserNotFoundException("User with id: %s is not found".formatted(id)));
    }

    public UserDto getUserById(Long id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new UserNotFoundException("User with id: %s is not found".formatted(id)));
    }
}
