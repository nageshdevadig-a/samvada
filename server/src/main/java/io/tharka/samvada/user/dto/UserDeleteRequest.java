package io.tharka.samvada.user.dto;

import io.tharka.samvada.core.validation.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDeleteRequest(

        @NotBlank @Email
        String email,

        @NotBlank
        @ValidPassword
        String password
) {
}
