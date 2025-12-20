package ir.hamedrostamkhani.backend.exception.resource;

import ir.hamedrostamkhani.backend.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceException extends BaseException {

    protected ResourceException(HttpStatus status, String message) {
        super(status, "Resource Error", message);
    }

    protected ResourceException(HttpStatus status, String message, Throwable cause) {
        super(status, "Resource Error", message, cause);
    }
}
