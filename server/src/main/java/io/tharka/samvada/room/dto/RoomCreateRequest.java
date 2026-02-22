package io.tharka.samvada.room.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RoomCreateRequest(
        @NotBlank
        @Email
        String targetEmail
) {}