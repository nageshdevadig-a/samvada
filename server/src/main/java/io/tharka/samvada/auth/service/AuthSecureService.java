package io.tharka.samvada.auth.service;

import io.jsonwebtoken.Claims;
import io.tharka.samvada.auth.dto.TokenResponse;
import io.tharka.samvada.auth.repository.RefreshTokenRepository;
import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.core.security.service.JWTService;
import io.tharka.samvada.user.dto.UserDeleteRequest;
import io.tharka.samvada.user.dto.UserPasswordUpdate;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.repository.UserRepository;
import io.tharka.samvada.user.repository.projections.PasswordProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthSecureService {

    @Value("${app.security.cookie-secure:true}")
    private boolean isSecure;

    @Value("${app.security.cookie-same-site:None}")
    private String isSameSite;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;


    @Transactional
    public TokenResponse disableUser(UserDeleteRequest user)
    {
        PasswordProjection userPassword = userRepository.findPasswordByEmail(user.email())
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(user.password(),userPassword.password()))
        {
           throw new BadCredentialsException("Invalid password");
        }
        refreshTokenRepository.deleteByUserEmail(user.email());
        userRepository.deactivateUser(user.email());
        return invalidateSession();
    }


    public boolean updatePassword(UserPasswordUpdate passwordRequest, UserPrincipal userPrincipal)
    {
        PasswordProjection oldPassword = userRepository.findPasswordByEmail(userPrincipal.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if(!passwordEncoder.matches(passwordRequest.currentPassword(),oldPassword.password()))
        {
            throw new BadCredentialsException("Invalid password");
        }
        return userRepository.updatePassword(
                userPrincipal.getEmail(),
                passwordEncoder.encode(passwordRequest.newPassword())
        );
    }


    public TokenResponse logoutUser(String jwtToken){
        String jti = jwtService.extractClaim(jwtToken, Claims::getId);
        refreshTokenRepository.deleteByJti(UUID.fromString(jti));
        return invalidateSession();

    }

    private TokenResponse invalidateSession()
    {
        ResponseCookie cookie1 =  ResponseCookie.from("rf_token","")
                .httpOnly(true)
                .secure(isSecure)
                .path("/api/v1/auth/refresh_token")
                .maxAge(0)
                .sameSite(isSameSite)
                .build();
        ResponseCookie cookie2 =  ResponseCookie.from("access_token","")
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(0)
                .sameSite(isSameSite)
                .build();
        ResponseCookie cookie3 =  ResponseCookie.from("isAuthenticated","")
                .httpOnly(false)
                .secure(isSecure)
                .path("/")
                .maxAge(0)
                .sameSite(isSameSite)
                .build();

        return new TokenResponse(cookie1.toString(), cookie2.toString(), cookie3.toString());
    }

}
