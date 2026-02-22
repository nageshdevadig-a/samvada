package io.tharka.samvada.core.exception;

import io.tharka.samvada.core.exception.base.RoomNotFoundException;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.tharka.samvada.core.exception.base.InvalidRefreshTokenException;
import io.tharka.samvada.core.exception.base.UserAlreadyExistsException;
import io.tharka.samvada.core.exception.base.UserNotFoundException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // General exception handler
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException() {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        return problemDetail;
    }

    // Security & Authentication
    @ExceptionHandler({
            ExpiredJwtException.class,
            SignatureException.class,
            MalformedJwtException.class,
            BadCredentialsException.class,
            InvalidRefreshTokenException.class,
    })
    public ProblemDetail buildAuthErrors(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Authentication Failed");
        if (exception instanceof BadCredentialsException)
            problemDetail.setDetail("Invalid username or password");
        else
            problemDetail.setDetail("Your Session is invalid or expired. Please login again.");
        return problemDetail;

    }

    // Validation and User related exceptions

    @Override
    protected @Nullable ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Invalid Input");
        problemDetail.setProperty("errors", errors);
        problemDetail.setTitle("Bad Request");
        return new ResponseEntity<>(problemDetail,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,ex.getMessage());
        problemDetail.setTitle("Conflict");
        return  problemDetail;
    }

    @ExceptionHandler({UserNotFoundException.class  , UsernameNotFoundException.class})
    public ProblemDetail handleUserNotFoundException()
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,"Username or password doesn't match");
        problemDetail.setTitle("Not Found");
        return  problemDetail;
    }

    @ExceptionHandler({RoomNotFoundException.class})
    public ProblemDetail handleRoomNotFoundException(RoomNotFoundException ex)
    {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,ex.getMessage());
        problemDetail.setTitle("Not Found");
        return  problemDetail;
    }


}