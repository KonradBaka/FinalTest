package pl.kurs.finaltest.exceptions;

import java.sql.Timestamp;
import java.util.List;

public class ExceptionsResponseDto {

    private List<String> errorMessages;
    private String errorCode;
    private Timestamp timestamp;

    public ExceptionsResponseDto(List<String> errorMessages, String errorCode, Timestamp timestamp) {
        this.errorMessages = errorMessages;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
