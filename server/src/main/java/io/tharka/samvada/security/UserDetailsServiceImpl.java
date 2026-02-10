package io.tharka.samvada.security;

import io.tharka.samvada.user.User;
import io.tharka.samvada.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<User> userEntity = userRepository.findById(new ObjectId(id));
        User user = userEntity.orElse(null);
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
