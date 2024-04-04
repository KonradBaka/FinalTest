package pl.kurs.finaltest.exceptions;

public class ImportInProgressException extends RuntimeException {

    public ImportInProgressException(String message) {
        super(message);
    }

    public ImportInProgressException(String message, Throwable cause) {
        super(message, cause);
    }
}
