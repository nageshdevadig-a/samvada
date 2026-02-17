package io.tharka.samvada.user.dto;

import io.tharka.samvada.core.validation.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record UserPasswordUpdate(
        @NotBlank
        @ValidPassword
        String currentPassword,

        @NotBlank
        @ValidPassword
        String newPassword
) {
}
