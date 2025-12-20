package ir.hamedrostamkhani.backend.exception.resource;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends ResourceException {

    public DuplicateResourceException(String message) {
        super(HttpStatus.CONFLICT, message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(HttpStatus.CONFLICT, message, cause);
    }
}
