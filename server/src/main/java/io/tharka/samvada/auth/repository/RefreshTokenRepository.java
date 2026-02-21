package io.tharka.samvada.auth.repository;


import io.tharka.samvada.auth.entity.RefreshToken;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends MongoRepository<@NonNull RefreshToken, @NonNull UUID> {

    Optional<RefreshToken> findByToken(UUID refreshToken);

    void deleteByUserEmail(String userEmail);

    void deleteByJti(UUID jti);
}
