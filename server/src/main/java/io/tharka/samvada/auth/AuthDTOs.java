package io.tharka.samvada.auth;

import io.tharka.samvada.core.validation.ValidIdentity;
import io.tharka.samvada.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthDTOs {
    /*
     * This record is used for creating a new user.
     * Record Args:
     * - name: Full name of the user
     * - username: Unique username for the user (alphanumeric and underscores only)
     * - email: Valid email address of the user
     * - password: Password for the user account (8-100 characters)
     *
     * */
    public record Create(
            @NotBlank
            String name,
            @NotBlank
            @Size(min = 3,max = 20)
            @Pattern(
                    regexp = "^[a-z][a-z0-9_]*$",
                    message = "Username can only contain alphanumeric characters and underscores"
            )
            String username,

            @NotBlank @Email
            String email,

            @NotBlank @Size(min = 8, max = 100)
            String password
    ){}


    /*
     * This record is used for user login.
     * Record Args:
     * - usernameOrEmail: Username or email of the user
     * - password: Password for the user account
     *
     * */
    public record Login(
            @NotBlank
            @Size(min = 3,max = 100)
            @ValidIdentity
            String usernameOrEmail,

            @NotBlank @Size(min = 8, max = 100)
            String password
    ){
        public static Login fromEntity(User user) {
            return new Login(
                    user.getEmail(),
                    user.getPassword()
            );
        }
    }

}
