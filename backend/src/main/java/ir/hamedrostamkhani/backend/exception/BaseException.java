package ir.hamedrostamkhani.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final String error;

    protected BaseException(HttpStatus status, String error, String message) {
        super(message);
        this.status = status;
        this.error = error;
    }

    protected BaseException(HttpStatus status, String error, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.error = error;
    }
}
