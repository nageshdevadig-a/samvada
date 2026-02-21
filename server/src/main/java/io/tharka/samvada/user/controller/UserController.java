package io.tharka.samvada.user.controller;

import io.tharka.samvada.user.dto.*;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.repository.UserRepository;
import io.tharka.samvada.user.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Valid
@RequestMapping("/api/v1/users/me")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;


    @GetMapping
    public ResponseEntity<@NonNull UserResponse> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        User user = userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow( ()-> new BadCredentialsException("Invalid username or password"));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new UserResponse(user.getUserName(), user.getEmail(), user.getFullName()));
    }



    @PutMapping
    public ResponseEntity<@NonNull UserResponse> updateUser(@Valid @RequestBody UserDetailsUpdate user,
                                        @AuthenticationPrincipal UserPrincipal userPrincipal)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUserDetails(user, userPrincipal));
    }


}


