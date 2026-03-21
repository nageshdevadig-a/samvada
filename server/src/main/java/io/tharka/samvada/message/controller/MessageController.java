package io.tharka.samvada.message.controller;

import io.tharka.samvada.message.dto.MessageRequest;
import io.tharka.samvada.message.dto.MessageResponse;
import io.tharka.samvada.message.service.MessageService;
import io.tharka.samvada.user.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/messages")
public class MessageController {

    private  final MessageService messageService;

    @GetMapping("/{roomId}")
    public ResponseEntity<@NonNull Slice<@NonNull MessageResponse>> getMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageService.getMessagesByRoomId(roomId, page, size));

    }

    @PostMapping("/{roomId}")
    public ResponseEntity<Void> postMessage(
            @PathVariable String roomId,
            @RequestBody @Valid MessageRequest message,
            @AuthenticationPrincipal UserPrincipal user
            )
    {
        messageService.saveAndNotify(roomId, message, user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
}
