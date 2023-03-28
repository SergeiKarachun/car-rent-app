package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RentalTimeBadRequestException extends ResponseStatusException {

    public RentalTimeBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public RentalTimeBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
