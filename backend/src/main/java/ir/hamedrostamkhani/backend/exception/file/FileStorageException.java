package ir.hamedrostamkhani.backend.exception.file;

import org.springframework.http.HttpStatus;

public class FileStorageException extends FileException {
    public FileStorageException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }
}
