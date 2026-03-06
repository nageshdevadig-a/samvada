package io.tharka.samvada.message.repository;

import io.tharka.samvada.message.entity.Message;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<@NonNull Message, @NonNull ObjectId> {
    Slice<@NonNull Message> findByRoomId(ObjectId roomId, Pageable pageable);
}
