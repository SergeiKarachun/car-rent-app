package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderBadRequestException extends ResponseStatusException {

    public OrderBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
    public OrderBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
