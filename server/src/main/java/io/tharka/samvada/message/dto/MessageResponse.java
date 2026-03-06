package io.tharka.samvada.message.dto;

import io.tharka.samvada.message.entity.Message;
import lombok.NonNull;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public record MessageResponse(
        String messageId,
        String senderEmail,
        String content,
        LocalDateTime sentAt
) {
    public static Slice<@NonNull MessageResponse> buildSliceFrom(Slice<@NonNull Message> messages)
    {
        return messages.map(message -> new MessageResponse(
                    message.getMessageId().toHexString(),
                    message.getSenderEmail(),
                    message.getContent(),
                    message.getSentAt()
            )
        );
    }

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getMessageId().toHexString(),
                message.getSenderEmail(),
                message.getContent(),
                message.getSentAt()
        );
    }
}
