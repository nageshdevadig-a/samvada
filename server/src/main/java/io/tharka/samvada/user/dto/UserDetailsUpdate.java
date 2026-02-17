package io.tharka.samvada.user.dto;

import jakarta.validation.constraints.*;

public record UserDetailsUpdate(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z\\s]*$",
                message = "Name must start with a letter and contain only alphabets and spaces"
        )
        String name,

        @NotBlank
        @Size(min = 3,max = 20)
        @Pattern(
                regexp = "^[a-z][a-z0-9_]*$",
                message = "Username can only contain alphanumeric characters and underscores"
        )
        String username
) {
}
