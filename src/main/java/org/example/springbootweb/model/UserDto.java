package org.example.springbootweb.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {

    @Null
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @Min(value = 7)
    @Max(value = 100)
    private Integer age;

    @Null
    private List<PetDto> pets;
}
