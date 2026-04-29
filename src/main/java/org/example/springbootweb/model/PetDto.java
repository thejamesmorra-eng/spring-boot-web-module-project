package org.example.springbootweb.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PetDto {

    @Null
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;

    @Null
    private Long userId;
}
