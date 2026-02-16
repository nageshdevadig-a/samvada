package io.tharka.samvada.auth.service;

import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        return checkUser(user);
    }

    public UserPrincipal loadUserByUserId(String id) throws UsernameNotFoundException
    {
        if(!ObjectId.isValid(id)){
            throw new UsernameNotFoundException("Invalid user id");
        }
        User user = userRepository.findById(new ObjectId(id)).orElse(null);
        return checkUser(user);
    }

    private UserPrincipal checkUser(User user)
    {
        if (user == null) {
            throw new UsernameNotFoundException(" User not found");
        }
        if(!user.isActive())
        {
            user.setActive(true);
            userRepository.save(user);
        }
        return new UserPrincipal(user);
    }

}
