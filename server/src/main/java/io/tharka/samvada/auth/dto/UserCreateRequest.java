package io.tharka.samvada.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * This record is used for creating a new user.
 * @param name Full name of the user
 * @param username Unique username for the user (alphanumeric and underscores only)
 * @param email Valid email address of the user
 * @param password Password for the user account (8-100 characters)
 */
public record UserCreateRequest(
        @NotBlank
        String name,
        @NotBlank
        @Size(min = 3,max = 20)
        @Pattern(
                regexp = "^[a-z][a-z0-9_]*$",
                message = "Username can only contain alphanumeric characters and underscores"
        )
        String username,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, max = 100)
        String password
) {}
