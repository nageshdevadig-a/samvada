package io.tharka.samvada.room.repository;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRoomRepositoryImpl  implements CustomRoomRepository {

    private final MongoTemplate mongoTemplate;


    @Override
    public List<String> findAllParticipantsUsernameByRoomId(ObjectId roomId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(roomId)),
                Aggregation.unwind("participants"),
                Aggregation.project().and("participants.userName").as("userName"),
                Aggregation.group().push("userName").as("userName")
        );

        Document result = mongoTemplate.aggregate(aggregation, "rooms", Document.class).getUniqueMappedResult();

        if (result != null && result.containsKey("userName")) {
            return result.getList("userName", String.class);
        }
        return Collections.emptyList();
    }
}
