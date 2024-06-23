package com.interview.userregister.controller;

import com.interview.userregister.exception.UserRegisterException;
import com.interview.userregister.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class UserRegisterExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.warn("MethodArgumentNotValidException", ex);

        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(e -> sb.append(e.getDefaultMessage()).append("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message(sb.toString())
                        .build());
    }

    @ExceptionHandler(UserRegisterException.class)
    public ResponseEntity<?> handleUserRegisterException(final UserRegisterException ex) {
        log.warn("UserRegisterException", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(final Exception ex) {
        log.warn("Exception", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .build());
    }
}
