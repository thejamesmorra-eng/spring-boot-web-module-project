package org.example.springbootweb.controller;

import jakarta.validation.Valid;
import org.example.springbootweb.model.PetDto;
import org.example.springbootweb.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/pets")
public class PetController {

    private static final Logger log = LoggerFactory.getLogger(PetController.class);
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetDto> createPet(@PathVariable Long userId, @Valid @RequestBody PetDto petDto) {
        log.info("Get request for create pet: {}", petDto);
        PetDto createdPet = petService.createPet(userId, petDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long userId, @PathVariable Long petId) {
        petService.deletePet(userId, petId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{petId}")
    public PetDto getPetById(@PathVariable Long petId) {
        return null;
    }
}
