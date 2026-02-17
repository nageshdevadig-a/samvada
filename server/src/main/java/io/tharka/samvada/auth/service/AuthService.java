package io.tharka.samvada.auth.service;


import io.tharka.samvada.auth.dto.TokenResponse;
import io.tharka.samvada.auth.dto.UserCreateRequest;
import io.tharka.samvada.auth.dto.UserLoginRequest;
import io.tharka.samvada.auth.entity.RefreshToken;
import io.tharka.samvada.auth.repository.RefreshTokenRepository;
import io.tharka.samvada.core.exception.base.InvalidRefreshTokenException;
import io.tharka.samvada.core.exception.base.UserAlreadyExistsException;
import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.core.security.service.JWTService;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AccountExpiredException;
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

    @Value("${app.security.cookie-secure:true}")
    private boolean isSecure;

    /**
     * It allows to create new users after it checks for the below conditions.
     * <p>
     * <ul>
     * <li>1. The user already exists in DB and status is active.</li>
     * <li>2. The user exists in DB but status is inactive.</li>
     * <li>3. The user request is new and have to persist the user record in DB.</li>
     * </ul>
     * </p>
     * @param user The user details to be registered.
     * @return {@code User} entity
     */
    public User register(UserCreateRequest user)
    {
        // TODO : first front end sends email and full name. then we send a emai to the email. of user is already exists then we send a mail as
        //  "Hello USERNAME,
        //  We received a request to create a new account with Samvada, but a user with this email already exists.
        //  Click below to log into your existing account.
        //  Log In(email id filled but user has to enter the password)
        //  To create a new account, click here.(user has to enter a new email id else fall back)rate limit to create new user with 3 for a day.
        //  If your request to create a new account was done in error, you can safely ignore this message.
        //  Thank you,
        //  The samvada Team"
        //  Dont throw user already exists exception. if user verify email successfully then we allow user to crate user name and password. if username already exists we say username is invalid
        //  after successfully user creation we send jwt cookie with access token and refresh token cookie.

        Optional<User> existingUser = userRepository.findByUsernameOrEmail(user.username(), user.email());

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




    public TokenResponse verify(UserLoginRequest user)
    {
        try {
            return authenticate(user);
        }
        catch (AccountExpiredException e){
            User userEntity = userRepository.findByUsernameOrEmail(user.usernameOrEmail(), user.usernameOrEmail())
                    .orElseThrow(()-> new BadCredentialsException("Invalid username or password."));
            userEntity.setActive(true);
            userEntity.setExpiresAt(null);
            userRepository.save(userEntity);
            return authenticate(user);
        }
        catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password.");
        }
    }

    private TokenResponse authenticate(UserLoginRequest user)
    {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user.usernameOrEmail(),
                        user.password()));
        if (!authentication.isAuthenticated()) {
            throw new UserNotFoundException("Invalid username or password.");
        }
        UserPrincipal userPrincipal = (UserPrincipal) Objects.requireNonNull(authentication.getPrincipal());
        return new TokenResponse(rfCookieBuilder(
                userPrincipal.getEmail()),
                jwtCookieBuilder(jwtService.generateToken(userPrincipal.getEmail()))
        );
    }


    public TokenResponse rfTokenVerify(String refreshToken, String jwtToken)
    {
        RefreshToken rfToken =  refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new InvalidRefreshTokenException("Invalid token. Please Login again"));
        if(rfToken.getExpiresAt().isBefore(Instant.now())){
            refreshTokenRepository.delete(rfToken);
            throw new InvalidRefreshTokenException("Session Expired. Please Login again");
        }
        refreshTokenRepository.delete(rfToken);
        jwtService.isSignatureValid(jwtToken);
        return new TokenResponse(rfCookieBuilder(
                rfToken.getUserEmail()),
                jwtCookieBuilder(jwtService.generateToken(rfToken.getUserEmail()))
        );
    }

    private String jwtCookieBuilder(String jwt)
    {
        // TODO change cookie name to __HOST-access_token to prevent the Cross Subdomain XSS attack
        ResponseCookie cookie =  ResponseCookie.from("access_token",jwt)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(3600)
                .sameSite("None")
                .build();
        return cookie.toString();
    }


    private String rfCookieBuilder(String userEmail)
    {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userEmail(userEmail)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        // TODO change cookie name to __HOST-access_token to prevent the Cross Subdomain XSS attack
        ResponseCookie cookie =  ResponseCookie.from("rf_token",refreshTokenEntity.getToken())
                .httpOnly(true)
                .secure(isSecure)
                .path("/api/v1/auth/refresh_token")
                .maxAge(TimeUnit.DAYS.toSeconds(7))
                .sameSite("None")
                .build();
        return cookie.toString();
    }




}
