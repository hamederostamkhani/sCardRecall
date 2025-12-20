package ir.hamedrostamkhani.backend.exception.resource;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ResourceException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }
}