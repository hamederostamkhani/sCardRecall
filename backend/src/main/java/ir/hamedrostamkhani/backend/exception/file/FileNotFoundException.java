package ir.hamedrostamkhani.backend.exception.file;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends FileException {
    public FileNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }
}
