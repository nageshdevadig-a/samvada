package io.tharka.samvada.room.service;

import io.tharka.samvada.core.exception.base.RoomNotFoundException;
import io.tharka.samvada.room.dto.Participant;
import io.tharka.samvada.room.dto.RoomCreateRequest;
import io.tharka.samvada.room.dto.RoomResponse;
import io.tharka.samvada.room.dto.UserRoomList;
import io.tharka.samvada.room.entity.Room;
import io.tharka.samvada.room.repository.RoomRepository;
import io.tharka.samvada.user.dto.UserResponse;
import io.tharka.samvada.user.model.UserPrincipal;
import io.tharka.samvada.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    public String getOrCreateDirectChatRoom(UserPrincipal user, RoomCreateRequest room) {
        String hash = generateUniqueHash(user.getEmail(), room.targetEmail());

        Room roomEntity = roomRepository.findByRoomHashCode(hash)
                .orElseGet(() -> createNewRoom(user, room, hash));
        return roomEntity.getRoomId().toHexString();

    }

    private Room createNewRoom(UserPrincipal user, RoomCreateRequest room, String hash) {
        UserResponse otherUser = userService.getUserDetails(room.targetEmail()); // Other person full name will be the room name for this person
        List<Participant>  participants = new ArrayList<>();
        participants.add(new Participant(user.getFullName(), user.getUsername(),user.getEmail(), LocalDateTime.now(),true));
        participants.add(new Participant(otherUser.fullName(),otherUser.userName(),otherUser.email(), LocalDateTime.now(),true));
        Room roomEntity = Room.builder()
                .roomHashCode(hash)
                .participants(participants)
                .build();
        return roomRepository.save(roomEntity);
    }

    private String generateUniqueHash(String email1, String email2) {
        List<String> emails = Arrays.asList(email1, email2);
        Collections.sort(emails);
        String joinEmails = String.join("-", emails);
        return DigestUtils.sha256Hex(joinEmails);

    }

    public RoomResponse getRoomById(String roomId, String requesterEmail) {
        if (!ObjectId.isValid(roomId)) { throw new IllegalArgumentException("Invalid room id"); }
        Room roomEntity = roomRepository.findById(new ObjectId(roomId))
                .orElseThrow(RoomNotFoundException::new);
        String roomName = roomEntity.getParticipants().stream()
                .filter(p -> !p.email().equals(requesterEmail))
                .map(Participant::fullName)
                .findFirst()
                .orElse("Unknown");
        return new RoomResponse(
                roomEntity.getRoomId().toHexString(),
                roomName,
                roomEntity.getParticipants()
        );
    }

    public List<UserRoomList> getMyRooms(String email) {
        return roomRepository.findByParticipantsEmail(email).stream()
                .map(room -> {
                    String roomName = room.getParticipants().stream()
                            .filter(p -> !p.email().equals(email))
                            .map(Participant::fullName)
                            .findFirst()
                            .orElse("Unknown");
                    return new UserRoomList(room.getRoomId().toHexString(), roomName);
                }).toList();
    }
}
