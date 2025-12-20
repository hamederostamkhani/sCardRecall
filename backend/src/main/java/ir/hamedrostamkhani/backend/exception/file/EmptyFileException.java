package ir.hamedrostamkhani.backend.exception.file;

import org.springframework.http.HttpStatus;

public class EmptyFileException extends FileException {
    public EmptyFileException() {
        super(HttpStatus.BAD_REQUEST, "Uploaded file is empty");
    }

    public EmptyFileException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public EmptyFileException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }
}
