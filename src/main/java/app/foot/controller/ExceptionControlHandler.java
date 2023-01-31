package app.foot.controller;

import app.foot.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControlHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestHandler(BadRequestException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
