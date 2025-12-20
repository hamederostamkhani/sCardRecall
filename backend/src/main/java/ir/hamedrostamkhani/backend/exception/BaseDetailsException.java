package ir.hamedrostamkhani.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public abstract class BaseDetailsException extends BaseException {

    private final Map<String, String> details;

    protected BaseDetailsException(HttpStatus status, String error, String message, Map<String, String> details) {
        super(status, error, message);
        this.details = Map.copyOf(details); // Make it immutable
    }

    protected BaseDetailsException(HttpStatus status, String error, String message, Map<String, String> details, Throwable cause) {
        super(status, error, message, cause);
        this.details = Map.copyOf(details); // Make it immutable
    }
}
