package fii.ai.natural.language.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid move!")
public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException() {
        this("Invalid move!");
    }

    public InvalidMoveException(String message) {
        super("The move " + message + " is not a valid move!");
    }
}
