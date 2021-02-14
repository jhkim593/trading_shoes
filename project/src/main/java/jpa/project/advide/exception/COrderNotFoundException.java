package jpa.project.advide.exception;

public class COrderNotFoundException extends RuntimeException{

        public COrderNotFoundException(String msg, Throwable t) {
            super(msg,t);}
        public COrderNotFoundException(String msg) {
            super(msg);
        }
        public COrderNotFoundException() {
            super();
        }

}
