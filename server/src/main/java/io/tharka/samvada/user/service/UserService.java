package io.tharka.samvada.user.service;

import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.user.dto.*;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean disableUser(UserDeleteRequest user)
    {
        User userEntity = userRepository.findByEmail(user.email())
                .orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(user.password(),userEntity.getPassword()))
        {
        userEntity.setActive(false);
        userEntity.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        userRepository.save(userEntity);
        return true;
        }
        return false;
    }

    public UserResponse updateUserDetails(UserDetailsUpdate user, UserPrincipal userPrincipal)
    {
        User userEntity = userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(UserNotFoundException::new);

        userEntity.setFullName(user.fullName());
        userEntity.setUserName(user.userName());
        User user1 = userRepository.save(userEntity);
        return new UserResponse(user1.getUserName(), user1.getEmail(), user1.getFullName());
    }


    public boolean updatePassword(UserPasswordUpdate passwordRequest, UserPrincipal userPrincipal)
    {
        User userEntity = userRepository.findByEmail(userPrincipal.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if(passwordEncoder.matches(passwordRequest.currentPassword(),userEntity.getPassword()))
        {
            userEntity.setPassword(passwordEncoder.encode(passwordRequest.newPassword()));
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }
}

