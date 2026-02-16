package io.tharka.samvada.auth.controller;


import io.tharka.samvada.auth.dto.TokenResponse;
import io.tharka.samvada.auth.dto.UserCreateRequest;
import io.tharka.samvada.auth.dto.UserLoginRequest;
import io.tharka.samvada.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@Valid
@RequiredArgsConstructor
public class AuthController
{

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserCreateRequest user)
    {
        TokenResponse tokens = authService.verify(UserLoginRequest.from(authService.register(user).getEmail(),user.password()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest user)
    {
        TokenResponse tokens = authService.verify(user);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

    @GetMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "rf_token") String refreshToken,
            @CookieValue(name = "access_token") String jwtToken
    )
    {
        TokenResponse tokens = authService.rfTokenVerify(refreshToken, jwtToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

}
