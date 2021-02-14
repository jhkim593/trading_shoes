package jpa.project.advide.exception;

public class CUserAlreadyExistException extends RuntimeException {
    public CUserAlreadyExistException() {
    }

    public CUserAlreadyExistException(String message) {
        super(message);
    }

    public CUserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
