package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccidentBadRequestException extends ResponseStatusException {

    public AccidentBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public AccidentBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
