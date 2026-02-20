package io.tharka.samvada.user.dto;

public record UserResponse(
        String userName,
        String email,
        String fullName
) {}
