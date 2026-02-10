package io.tharka.samvada.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.bson.types.ObjectId;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean deleteUser(String userId)
    {
        if(ObjectId.isValid(userId))
        {
            ObjectId id = new ObjectId(userId);
            Optional<User> userEntity = userRepository.findById(id);
            User user = userEntity.orElse(null);
            if(user != null)
            {
            user.setActive(false);
            userRepository.save(user);
            return true;
            }
        }
        return false;
    }

}

