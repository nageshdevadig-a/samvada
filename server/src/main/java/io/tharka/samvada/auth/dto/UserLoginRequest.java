package io.tharka.samvada.auth.dto;

import io.tharka.samvada.core.validation.annotation.ValidIdentity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * This record is used for user login.
 * @param usernameOrEmail: Username or email of the user
 * @param password: Password for the user account
 * */
public record UserLoginRequest(
       @NotBlank
       @Size(min = 3,max = 100)
       @ValidIdentity
       String usernameOrEmail,

       @NotBlank @Size(min = 8, max = 100)
       String password
)
{
//    public static UserLoginRequest fromEntity(User user) {
//        return new UserLoginRequest(
//                user.getEmail(),
//                user.getPassword()
//        );
//    }
    public static UserLoginRequest from(String email, String password) {
        return new UserLoginRequest(
                email,
                password
        );
    }

}
