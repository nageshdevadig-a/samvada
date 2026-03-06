package io.tharka.samvada.message.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
@CompoundIndex(name = "roomId_time_idx", def = "{'roomId': 1, 'createdAt': -1}")
public class Message {

    @Id
    private ObjectId messageId;

    private String senderEmail;

    private ObjectId roomId;

    private String content;

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();
}
