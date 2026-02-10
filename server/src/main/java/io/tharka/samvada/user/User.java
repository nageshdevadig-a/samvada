package io.tharka.samvada.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private boolean active = true;


}

