package pl.kurs.finaltest.exceptions;

public class SessionNotFoundException extends RuntimeException{

    public SessionNotFoundException(String message) {
        super(message);
    }

    public SessionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
