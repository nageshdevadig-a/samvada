package io.tharka.samvada.user.service;

import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.user.dto.*;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.repository.UserRepository;
import io.tharka.samvada.user.repository.projections.PasswordProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean disableUser(UserDeleteRequest user)
    {
        PasswordProjection userPassword = userRepository.findPasswordByEmail(user.email())
                .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(user.password(),userPassword.password()))
        {
        return userRepository.deactivateUser(user.email());
        }
        return false;
    }

    public UserResponse updateUserDetails(UserDetailsUpdate user, UserPrincipal userPrincipal)
    {
        return userRepository.updateAndFetchUser(
                userPrincipal.getEmail(),
                user.userName(),
                user.fullName()
        ).orElseThrow(UserNotFoundException::new);
    }


    public boolean updatePassword(UserPasswordUpdate passwordRequest, UserPrincipal userPrincipal)
    {
        PasswordProjection oldPassword = userRepository.findPasswordByEmail(userPrincipal.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if(passwordEncoder.matches(passwordRequest.currentPassword(),oldPassword.password()))
        {
            return userRepository.updatePassword(
                    userPrincipal.getEmail(),
                    passwordEncoder.encode(passwordRequest.newPassword())
            );
        }
        return false;
    }
}

