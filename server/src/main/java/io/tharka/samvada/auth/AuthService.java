package io.tharka.samvada.auth;


import io.tharka.samvada.core.exception.InvalidRefreshTokenException;
import io.tharka.samvada.core.exception.UserAlreadyExistsException;
import io.tharka.samvada.core.exception.UserNotFoundException;
import io.tharka.samvada.security.JWTService;
import io.tharka.samvada.security.UserPrincipal;
import io.tharka.samvada.user.User;
import io.tharka.samvada.user.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService
{

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;



    User register(AuthDTOs.Create user)
    {

//        System has to check for the 3 conditions before creating a new user in database.
//        1. The user already exists in DB and status is active.
//        2. The user exists in DB but status is inactive.
//        3. The user request is new and have to persist the user record in DB.

        // Ensure username or email id not exists in DB before creating a new user.
        Optional<User> existingUser = Optional.ofNullable(
                userRepository.findByUsernameOrEmail(user.username(), user.email()));

        if (existingUser.isPresent())
        {
            User userEntity = existingUser.get();

            if (userEntity.isActive())
            {
                // Case 1: User exists and is active
                throw new UserAlreadyExistsException("User with given username or email already exists.");
            }
            else
            {
                // Case 2: User exists but is inactive
                throw new UserAlreadyExistsException("Account already exists. Login to reactivate your account.");
            }

        }
        // Case 3: New user request, create and persist the user record.
        User newUser = User.builder()
                .name(user.name())
                .username(user.username())
                .email(user.email())
                .password(passwordEncoder.encode(user.password()))
                .build();
        return userRepository.save(newUser);


    }




    AuthDTOs.AccessToken verify(AuthDTOs.Login user)
    {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            user.usernameOrEmail(),
                            user.password()));
            if (!authentication.isAuthenticated()) {
                throw new UserNotFoundException("Invalid username or password.");
            }
            UserPrincipal userPrincipal = (UserPrincipal) Objects.requireNonNull(authentication.getPrincipal());
            return new AuthDTOs.AccessToken(rfCookieBuilder(
                    userPrincipal.getId()),
                    jwtCookieBuilder(jwtService.generateToken(userPrincipal))
            );
        }
        catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password.");
        }

    }


    AuthDTOs.AccessToken rfTokenVerify(String refreshToken)
    {
        RefreshToken rfToken =  refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new InvalidRefreshTokenException("Invalid token. Please Login again"));
        if(rfToken.getExpiresAt().isBefore(Instant.now())){
            refreshTokenRepository.delete(rfToken);
            throw new InvalidRefreshTokenException("Session Expired. Please Login again");
        }
        refreshTokenRepository.delete(rfToken);
        UserPrincipal userPrincipal = new UserPrincipal(userRepository.findById(new ObjectId(rfToken.getUserId()))
                .orElseThrow(() -> new UserNotFoundException("User not found for the given refresh token.")));
        return new AuthDTOs.AccessToken(rfCookieBuilder(
                userPrincipal.getId()),
                jwtCookieBuilder(jwtService.generateToken(userPrincipal))
        );
    }

    private String jwtCookieBuilder(String jwt)
    {
        ResponseCookie cookie =  ResponseCookie.from("access_token",jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();
        return cookie.toString();
    }


    private String rfCookieBuilder(String userId)
    {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(userId)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        ResponseCookie cookie =  ResponseCookie.from("rf_token",refreshTokenEntity.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh_token")
                .maxAge(TimeUnit.DAYS.toSeconds(7))
                .sameSite("Strict")
                .build();
        return cookie.toString();
    }




}
