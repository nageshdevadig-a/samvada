package io.tharka.samvada.user.repository;

import io.tharka.samvada.user.dto.UserResponse;


import java.util.Optional;

public interface CustomUserRepository {
    boolean updatePassword(String email, String newPassword);
    Optional<UserResponse> updateAndFetchUser(String email, String userName, String fullName);

    boolean deactivateUser( String email);
}
