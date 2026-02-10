package io.tharka.samvada.user;

//import io.tharka.samvada.core.exception.UserNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Valid
@RequestMapping("/api/v1/users/me")
public class UserController {

    private final UserService userService;


//
//    @DeleteMapping
//    public  ResponseEntity<?> deleteUser(@PathVariable String userId)
//    {
//        boolean isDeleted = userService.deleteUser(userId);
//
//        if(!isDeleted)
//        {
//            throw new UserNotFoundException("Cannot delete: User not found");
//        }
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }



}


