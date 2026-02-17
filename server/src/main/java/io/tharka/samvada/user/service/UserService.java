package io.tharka.samvada.user.service;

import io.tharka.samvada.core.exception.base.UserNotFoundException;
import io.tharka.samvada.user.dto.UserDeleteRequest;
import io.tharka.samvada.user.entity.User;
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
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        if (passwordEncoder.matches(user.password(),userEntity.getPassword()))
        {
        userEntity.setActive(false);
        userEntity.setExpiresAt(Instant.now().plus(30, ChronoUnit.DAYS));
        userRepository.save(userEntity);
        return true;
        }
        return false;
    }

}

