package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CategoryBadRequestException extends ResponseStatusException {

    public CategoryBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public CategoryBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
