package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserBadRequestException extends ResponseStatusException {

    public UserBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
    public UserBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
