package io.tharka.samvada.room.dto;

import java.util.List;

public record RoomResponse(
        String roomId,
        String roomName,
        List<Participant> participant
) {}
