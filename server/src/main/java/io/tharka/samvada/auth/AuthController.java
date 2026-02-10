package io.tharka.samvada.auth;


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
    public ResponseEntity<?> signUp(@Valid @RequestBody AuthDTOs.Create user)
    {
        AuthDTOs.AccessToken tokens = authService.verify(AuthDTOs.Login.fromEntity(authService.register(user)));
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTOs.Login user)
    {
        AuthDTOs.AccessToken tokens = authService.verify(user);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

    @GetMapping("/refresh_token")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "rf_token", required = false) String refreshToken
    )
    {

        AuthDTOs.AccessToken tokens = authService.rfTokenVerify(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokens.jwtToken())
                .header(HttpHeaders.SET_COOKIE, tokens.refreshToken())
                .build();
    }

}
