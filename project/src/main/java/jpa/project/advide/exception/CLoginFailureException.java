package jpa.project.advide.exception;

public class CLoginFailureException extends RuntimeException {
    public CLoginFailureException() {
    }

    public CLoginFailureException(String message) {
        super(message);
    }

    public CLoginFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}