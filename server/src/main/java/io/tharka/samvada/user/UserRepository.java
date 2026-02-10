package io.tharka.samvada.user;

import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface UserRepository extends MongoRepository<@NonNull User, @NonNull ObjectId> {

    User findByEmail(@NonNull String email);
    User findByUsername(@NonNull String username);
    User findByUsernameOrEmail(@NonNull String username, @NonNull String email);
}
