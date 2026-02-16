package io.tharka.samvada.auth.dto;

import jakarta.validation.constraints.NotBlank;


/**
 * This record is used to return the tokens from service layer to controller
 * to form the http header of the cookie.
 * @param refreshToken
 * @param jwtToken
 */
public record TokenResponse(
        @NotBlank
        String refreshToken,
        @NotBlank
        String jwtToken
) {}
