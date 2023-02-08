package app.foot.controller;

import app.foot.exception.BadRequestException;
import app.foot.exception.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BaseController {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<BadRequestException> handleException(BadRequestException e) {
        log.info("A bad request exception was handled");
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({InternalServerException.class})
    public ResponseEntity<InternalServerException> handleException(InternalServerException e) {
        log.info("A internal server request exception was handled");
        return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
