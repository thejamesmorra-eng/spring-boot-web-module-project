package org.example.springbootweb.service;

import org.example.springbootweb.exceptionHandler.exceptions.AccessDeniedException;
import org.example.springbootweb.exceptionHandler.exceptions.PetNotFoundException;
import org.example.springbootweb.model.PetDto;
import org.example.springbootweb.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PetService {

    private Long nextId;
    private final Map<Long, PetDto> pets;
    private final UserService userService;

    public PetService(UserService userService) {
        nextId = 0L;
        this.pets = new HashMap<>();
        this.userService = userService;
    }

    public PetDto createPet(Long userId, PetDto petDto) {
        UserDto userDto = userService.getUserById(userId);
        PetDto newPetDto = new PetDto(
                ++nextId,
                petDto.getName(),
                userDto.getId());
        pets.put(newPetDto.getId(), newPetDto);
        userDto.getPets().add(newPetDto);
        return newPetDto;
    }

    public void deletePet(Long userId, Long petId) {
        UserDto userDto = userService.getUserById(userId);
        PetDto petDto = getPetById(petId);
        if (!Objects.equals(userDto.getId(), petDto.getUserId())) {
            throw new AccessDeniedException("User with id: %s does not own pet with id: %s".formatted(userId, petId));
        }
        userDto.getPets().remove(petDto);
        pets.remove(petId);
    }

    public PetDto getPetById(Long id) {
        return Optional.ofNullable(pets.get(id))
                .orElseThrow(() -> new PetNotFoundException("Pet with id: %s is not found".formatted(id)));
    }
}
