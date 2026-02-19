package io.tharka.samvada.auth.dto;

import io.tharka.samvada.core.validation.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * This record is used for creating a new user.
 * @param name Full name of the user
 * @param userName Unique username for the user (alphanumeric and underscores only)
 * @param email Valid email address of the user
 * @param password Password for the user account (8-100 characters)
 */
public record UserCreateRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z\\s]*$",
                message = "Name must start with a letter and contain only alphabets and spaces")
        String fullName,


        @NotBlank
        @Size(min = 3,max = 20)
        @Pattern(
                regexp = "^[a-z][a-z0-9_]*$",
                message = "Username can only contain alphanumeric characters and underscores")
        String userName,

        @NotBlank @Email
        String email,

        @NotBlank
        @ValidPassword
        String password
) {}
