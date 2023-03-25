package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserDetailsBadRequestException extends ResponseStatusException {

    public UserDetailsBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
    public UserDetailsBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
