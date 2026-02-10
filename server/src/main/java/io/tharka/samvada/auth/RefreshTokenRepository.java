package io.tharka.samvada.auth;


import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<@NonNull RefreshToken, @NonNull String> {

    Optional<RefreshToken> findByToken(String refreshToken);
}
