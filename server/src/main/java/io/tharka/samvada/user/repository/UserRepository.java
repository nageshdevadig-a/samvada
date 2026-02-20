package io.tharka.samvada.user.repository;

import io.tharka.samvada.user.dto.UserResponse;
import io.tharka.samvada.user.entity.User;
import io.tharka.samvada.user.repository.projections.PasswordProjection;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends CustomUserRepository,MongoRepository<@NonNull User, @NonNull ObjectId> {

    Optional<User> findByEmail(@NonNull String email);

    Optional<User> findByUserNameOrEmail(@NonNull String username, @NonNull String email);

    Optional<PasswordProjection> findPasswordByEmail(@NonNull String email);

    Optional<UserResponse> findUserResponseByEmail(@NonNull String email);
}
