package jpa.project.advide.exception;

public class CShoesAlreadyExistException extends RuntimeException {


        public CShoesAlreadyExistException() {
        }

        public CShoesAlreadyExistException(String message) {
            super(message);
        }

        public CShoesAlreadyExistException(String message, Throwable cause) {
            super(message, cause);
        }

}
