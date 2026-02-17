package io.tharka.samvada.user.entity;

import io.tharka.samvada.user.enums.Role;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;

    private String name;

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String username;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING)
    private String email;

    @Field("password_hash")
    private String password;


    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private Set<Role> roles = new HashSet<>(Set.of(Role.ROLE_USER));

    @Indexed(expireAfter = "0")
    private Instant expiresAt;

}

