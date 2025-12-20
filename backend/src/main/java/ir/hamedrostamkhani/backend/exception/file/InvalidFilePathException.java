package ir.hamedrostamkhani.backend.exception.file;

import org.springframework.http.HttpStatus;

public class InvalidFilePathException extends FileException {
    
    public InvalidFilePathException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public InvalidFilePathException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
