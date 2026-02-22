package io.tharka.samvada.room.entity;


import io.tharka.samvada.room.dto.Participant;
import io.tharka.samvada.room.enums.RoomType;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rooms")
@CompoundIndex(name = "participants_email_idx", def = "{'participants.email': 1}")
public class Room {

    @Id
    private ObjectId roomId;

    @Builder.Default
    private RoomType type = RoomType.DIRECT_MESSAGE;

    @Indexed(unique = true, sparse = true)
    private String roomHashCode;

    private List<Participant> participants;

}
