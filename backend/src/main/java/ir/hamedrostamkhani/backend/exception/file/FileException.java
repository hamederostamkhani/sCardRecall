package ir.hamedrostamkhani.backend.exception.file;

import ir.hamedrostamkhani.backend.exception.BaseException;
import org.springframework.http.HttpStatus;

public abstract class FileException extends BaseException {

    protected FileException(HttpStatus status, String message) {
        super(status, "File Error", message);
    }

    protected FileException(HttpStatus status, String message, Throwable cause) {
        super(status, "File Error", message, cause);
    }
}