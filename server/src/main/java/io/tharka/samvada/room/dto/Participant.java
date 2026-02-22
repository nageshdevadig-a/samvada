package io.tharka.samvada.room.dto;
import java.time.LocalDateTime;

public record Participant(
        String fullName,
        String userName,
        String email,
        LocalDateTime lastReadAt,
        boolean isAdmin
) {
}
