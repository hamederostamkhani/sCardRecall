package ir.hamedrostamkhani.backend.exception.file;

import org.springframework.http.HttpStatus;

public class FileSizeException extends FileException {

    public FileSizeException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public FileSizeException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
