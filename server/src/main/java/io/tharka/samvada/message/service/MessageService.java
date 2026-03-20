package io.tharka.samvada.message.service;

import io.tharka.samvada.message.dto.MessageRequest;
import io.tharka.samvada.message.dto.MessageResponse;
import io.tharka.samvada.message.entity.Message;
import io.tharka.samvada.message.repository.MessageRepository;
import io.tharka.samvada.room.service.RoomService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomService roomService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public Slice<@NonNull MessageResponse> getMessagesByRoomId(String roomId, int page, int size)
    {
        if (!ObjectId.isValid(roomId)) {throw new IllegalArgumentException("Invalid roomId format");}
        ObjectId roomId1 = new ObjectId(roomId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("sentAt").descending());

        return MessageResponse.buildSliceFrom(messageRepository.findByRoomId(roomId1, pageable));


    }

    public void saveAndNotify(String roomId, MessageRequest message, String senderEmail)
    {
        if (!ObjectId.isValid(roomId)) {throw new IllegalArgumentException("Invalid roomId format");}
        ObjectId roomId1 = new ObjectId(roomId);

        Message messageEntity = Message.builder()
                .roomId(roomId1)
                .senderEmail(senderEmail)
                .content(message.content())
                .build();
        messageRepository.save(messageEntity);
        List<String> participantUsernames = roomService.getMemberUsernames(roomId);
        for (String userName: participantUsernames) {
            simpMessagingTemplate.convertAndSendToUser(
                    userName,
                    "/queue/messages",
                    MessageResponse.from(messageEntity));
        }
    }
}
