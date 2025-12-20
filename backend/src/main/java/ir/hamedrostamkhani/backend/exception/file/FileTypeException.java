package ir.hamedrostamkhani.backend.exception.file;

import org.springframework.http.HttpStatus;

public class FileTypeException extends FileException {

    public FileTypeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public FileTypeException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
