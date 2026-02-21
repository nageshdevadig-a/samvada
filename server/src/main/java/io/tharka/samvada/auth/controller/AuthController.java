package io.tharka.samvada.auth.controller;


import io.tharka.samvada.auth.dto.TokenResponse;
import io.tharka.samvada.auth.dto.UserCreateRequest;
import io.tharka.samvada.auth.dto.UserLoginRequest;
import io.tharka.samvada.auth.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController
{

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserCreateRequest user,
                                       @RequestHeader("X-Device-ID") @Size(max = 32) String deviceId)
    {
        TokenResponse tokens = authService.verify(
                UserLoginRequest.from(authService.register(user).getEmail(),user.password()),
                deviceId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken(), tokens.refreshToken())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest user,
                                              @RequestHeader("X-Device-ID") @Size(max = 32) String deviceId)
    {
        TokenResponse tokens = authService.verify(user,deviceId);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken(), tokens.refreshToken())
                .build();
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<Void> refreshToken(
            @CookieValue(name = "rf_token") String refreshToken,
            @RequestHeader("X-Device-ID") @Size(max = 32) String deviceId
    )
    {
        TokenResponse tokens = authService.rfTokenVerify(refreshToken,deviceId);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

}
