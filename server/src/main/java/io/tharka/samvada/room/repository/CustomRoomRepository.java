package io.tharka.samvada.room.repository;

import org.bson.types.ObjectId;

import java.util.List;

public interface CustomRoomRepository {
    List<String> findAllParticipantsUsernameByRoomId(ObjectId roomId);
}
