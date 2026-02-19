package io.tharka.samvada.auth.service;

import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String usernameOrEmail) throws UsernameNotFoundException
    {
        User user = (userRepository.findByUserNameOrEmail(usernameOrEmail, usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException(" User not found"));
        return checkUser(user);
    }

    private UserPrincipal checkUser(User user)
    {
        return new UserPrincipal(user);
    }

}
