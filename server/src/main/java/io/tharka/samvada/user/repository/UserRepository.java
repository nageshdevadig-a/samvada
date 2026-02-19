package io.tharka.samvada.user.repository;

import io.tharka.samvada.user.entity.User;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<@NonNull User, @NonNull ObjectId> {

    Optional<User> findByEmail(@NonNull String email);
    User findByUserName(@NonNull String username);
    Optional<User> findByUserNameOrEmail(@NonNull String username, @NonNull String email);
}
