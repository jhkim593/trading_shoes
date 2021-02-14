package jpa.project.advide.exception;

public class CNotEnoughStockException extends RuntimeException{
    public CNotEnoughStockException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNotEnoughStockException(String msg) {
        super(msg);
    }

    public CNotEnoughStockException() {
        super();
    }
}
