package io.tharka.samvada.core.exception.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException() {super("Room not found");}
    public RoomNotFoundException(String message) {
        super(message);
    }
}
