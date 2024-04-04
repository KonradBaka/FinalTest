package pl.kurs.finaltest.exceptions;

public class InvalidInputData extends RuntimeException{

    public InvalidInputData(String message) {
        super(message);
    }

    public InvalidInputData(String message, Throwable cause) {
        super(message, cause);
    }
}
