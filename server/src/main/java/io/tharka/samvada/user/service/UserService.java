package io.tharka.samvada.user.service;

import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.user.dto.*;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public UserResponse updateUserDetails(UserDetailsUpdate user, UserPrincipal userPrincipal)
    {
        return userRepository.updateAndFetchUser(
                userPrincipal.getEmail(),
                user.userName(),
                user.fullName()
        ).orElseThrow(UserNotFoundException::new);
    }


    public UserResponse getUserDetails(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow( ()-> new BadCredentialsException("Invalid username or password"));
        return new UserResponse(user.getUserName(), user.getEmail(), user.getFullName());
    }

    public UserResponse searchUsers(String usernameOrEmail) {
        User user =  userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(" User not found"));

        return new UserResponse(user.getUserName(), user.getEmail(), user.getFullName());
    }
}

