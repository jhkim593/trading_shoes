package jpa.project.advide.exception;

public class CAlreadySoldException extends RuntimeException{
    public CAlreadySoldException(String msg, Throwable t) {
        super(msg, t);
    }
    public CAlreadySoldException(String msg) {
        super(msg);
    }
    public CAlreadySoldException() {
        super();
    }

}
