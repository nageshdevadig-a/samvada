package io.tharka.samvada.auth;

import io.tharka.samvada.core.validation.ValidIdentity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.tharka.samvada.user.User;
/**
 *
 * <p></p>This class is useful for parsing requests into objects and passing them for further operations.</p></br>
 * Records in AuthDTOs are:
 * <ul>
 *     <li>{@link Create}</li>
 *     <li>{@link Login}</li>
 *     <li>{@link AccessToken}</li>
 * </ul>
 */
public class AuthDTOs {
    /**
     * This record is used for creating a new user.
     * @param name Full name of the user
     * @param username Unique username for the user (alphanumeric and underscores only)
     * @param email Valid email address of the user
     * @param password Password for the user account (8-100 characters)
     */
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


    /**
     * This record is used for user login.
     * @param usernameOrEmail: Username or email of the user
     * @param password: Password for the user account
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

    /**
     * This record is used to return the tokens from service layer to controller
     * to form the http header of the cookie.
     * @param refreshToken
     * @param jwtToken
     */
    public record AccessToken(
            @NotBlank
            String refreshToken,
            @NotBlank
            String jwtToken
    ){}

}
