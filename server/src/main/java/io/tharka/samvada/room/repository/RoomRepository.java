package io.tharka.samvada.room.repository;

import io.tharka.samvada.room.entity.Room;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CustomRoomRepository, MongoRepository<@NonNull Room, @NonNull ObjectId> {

    Optional<Room> findByRoomHashCode(String roomHashCode);

    @Query("{'participants.email': ?0}")
    List<Room> findByParticipantsEmail(String email);
}
