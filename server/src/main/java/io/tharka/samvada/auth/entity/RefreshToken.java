package io.tharka.samvada.auth.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private UUID token;

    @Indexed(unique = true, sparse = true)
    private UUID jti;

    private String deviceId;

    @Indexed(unique = true, sparse = true)
    private String userEmail;

    @Builder.Default
    @Indexed(expireAfter = "0")
    private Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
}
