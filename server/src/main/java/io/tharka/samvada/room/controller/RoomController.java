package io.tharka.samvada.room.controller;


import io.tharka.samvada.room.dto.RoomCreateRequest;
import io.tharka.samvada.room.dto.RoomResponse;
import io.tharka.samvada.room.dto.UserRoomList;
import io.tharka.samvada.room.service.RoomService;
import io.tharka.samvada.user.model.UserPrincipal;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<@NonNull RoomResponse> createDirectChatRoom(
            @Valid @RequestBody RoomCreateRequest room,
            @AuthenticationPrincipal UserPrincipal user)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.getOrCreateDirectChatRoom(user, room));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<@NonNull RoomResponse> getRoom(
            @PathVariable String roomId,
            @AuthenticationPrincipal UserPrincipal user)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(roomService.getRoomById(roomId, user.getEmail()));

    }

    @GetMapping
    public ResponseEntity<@NonNull List<UserRoomList>> getRooms(@AuthenticationPrincipal UserPrincipal user)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(roomService.getMyRooms(user.getEmail()));
    }

}
