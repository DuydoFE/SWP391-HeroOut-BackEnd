package com.demo.demo.exception;

import com.demo.demo.exception.exceptions.AuthenticationException;
import com.demo.demo.exception.exceptions.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MyExceptionHandler {

    // ✅ 1. Lỗi validation @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBadRequestException(MethodArgumentNotValidException exception) {
        List<String> messages = exception.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
    }

    // ✅ 2. Lỗi do bạn tự throw ở Service
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(List.of("Lỗi dữ liệu: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // ✅ 3. Lỗi khi không tìm thấy entity
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(List.of("Không tìm thấy dữ liệu: " + ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    // ✅ 4. Lỗi xác thực custom
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // ✅ 5. Lỗi request custom
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException e) {
        return new ResponseEntity<>(List.of(e.getMessage()), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Lỗi hệ thống: " + ex.getMessage());
    }

}
