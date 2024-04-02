package pl.kurs.finaltest.exceptions;

public class FailedImportException extends RuntimeException{

    public FailedImportException(String message) {
        super(message);
    }

    public FailedImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
