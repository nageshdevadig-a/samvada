package io.tharka.samvada.core.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import lombok.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorDTO> handleGeneralException(Exception ex, HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred.",
                request.getRequestURI(),
                Map.of("server", ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request)
    {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "Invalid Input Data",
                request.getRequestURI(),
                errors
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<@NonNull ErrorDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request)
    {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "User Already Exists",
                "The identifier provided is already exists.",
                request.getRequestURI(),
                Map.of("user", ex.getMessage())
        );
    }

    @ExceptionHandler({UserNotFoundException.class  , UsernameNotFoundException.class})
    public ResponseEntity<@NonNull ErrorDTO> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request)
    {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "User Not Found",
                "Username or password doesn't match",
                request.getRequestURI(),
                Map.of("user", ex.getMessage())
        );
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<@NonNull ErrorDTO>  handleBadCredentialsException (BadCredentialsException ex, HttpServletRequest request)
    {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Bad Credentials",
                "Invalid username or password.",
                request.getRequestURI(),
                Map.of("user", ex.getMessage())
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<@NonNull ErrorDTO>  handleRefreshTokenException (InvalidRefreshTokenException ex, HttpServletRequest request)
    {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid Refresh Token",
                "Refresh Token is invalid.",
                request.getRequestURI(),
                Map.of("token", ex.getMessage())
        );
    }

    @ExceptionHandler({ExpiredJwtException.class, SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<@NonNull ErrorDTO>  handleJwtException (Exception ex, HttpServletRequest request)
    {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT Token",
                "JWT token is invalid or expired.",
                request.getRequestURI(),
                Map.of("token", ex.getMessage())
        );
    }


    private  ResponseEntity<@NonNull ErrorDTO> buildErrorResponse(
            HttpStatus status,
            String title,
            String detail,
            String path,
            Map<String, String> errors
            )
    {
        ErrorDTO errorBody = ErrorDTO.builder()
                .title(title)
                .status(status.value())
                .detail(detail)
                .instance(path)
                .timestamp(Instant.now())
                .errors(errors)
                .build();

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(errorBody);
    }

}