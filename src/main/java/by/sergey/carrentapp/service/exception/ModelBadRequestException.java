package by.sergey.carrentapp.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ModelBadRequestException extends ResponseStatusException {

    public ModelBadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
    public ModelBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
