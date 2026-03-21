package io.tharka.samvada.auth.controller;

import io.tharka.samvada.auth.dto.TokenResponse;
import io.tharka.samvada.auth.service.AuthSecureService;
import io.tharka.samvada.user.dto.UserDeleteRequest;
import io.tharka.samvada.user.dto.UserPasswordUpdate;
import io.tharka.samvada.user.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Valid
@RequiredArgsConstructor
public class AuthSecureController {

    private final AuthSecureService authSecureService;

    @PostMapping("/deactivate")
    public  ResponseEntity<@NonNull Void> deactivateUser(@Valid @RequestBody UserDeleteRequest user)
    {
        TokenResponse tokens = authSecureService.disableUser(user);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header(HttpHeaders.SET_COOKIE, tokens.jwtToken(), tokens.refreshToken())
                    .build();
    }

    @PutMapping("/password")
    public ResponseEntity<@NonNull Map<String, String>> updatePassword(@Valid @RequestBody UserPasswordUpdate passwordRequest,
                                                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (authSecureService.updatePassword(passwordRequest, userPrincipal))
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Password updated successfully"));
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid Current Password"));
    }

    @PostMapping("/logout")
    public ResponseEntity<@NonNull Void> logout(@CookieValue(name = "access_token") String jwtToken) {
        TokenResponse tokens = authSecureService.logoutUser(jwtToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken(), tokens.refreshToken(),tokens.isAuthenticated())
                .build();

    }
}
