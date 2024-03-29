package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CarBadRequestException extends ResponseStatusException {

    public CarBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public CarBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
