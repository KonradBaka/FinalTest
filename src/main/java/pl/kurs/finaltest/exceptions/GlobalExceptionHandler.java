package pl.kurs.finaltest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionsResponseDto> handleValidationException(MethodArgumentNotValidException e) {
        ExceptionsResponseDto responseDto = new ExceptionsResponseDto(
                getMessagesList(e),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ExceptionsResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        ExceptionsResponseDto responseDto = new ExceptionsResponseDto(
                List.of(e.getMessage()),
                "NOT_FOUND",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }

    @ExceptionHandler({FailedImportException.class})
    public ResponseEntity<ExceptionsResponseDto> handleFailedImportException(FailedImportException e) {
        ExceptionsResponseDto responseDto = new ExceptionsResponseDto(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                Timestamp.from(Instant.now())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }


    private List<String> getMessagesList(MethodArgumentNotValidException e) {
        return e.getFieldErrors()
                .stream()
                .map(fe -> "filed: " + fe.getField() + " / rejected value: '" + fe.getRejectedValue() + "' / message: " + fe.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
