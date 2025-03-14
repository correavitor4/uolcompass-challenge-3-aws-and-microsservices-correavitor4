package compassoulspring2024pb.challenge1.eventservice.exception.api.handler;

import compassoulspring2024pb.challenge1.eventservice.exception.api.EntityNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.exception.api.EventDeletionException;
import compassoulspring2024pb.challenge1.eventservice.exception.api.message.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class APIExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception,
                                                                      HttpServletRequest request) {
        printAPIError(exception.getMessage());

        HttpStatus ht = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(ht)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, ht, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException exception,
                                                                  HttpServletRequest request) {
        printAPIError(exception.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, status, "Error validating request", exception.getBindingResult()));
    }

    @ExceptionHandler(EventDeletionException.class)
    public ResponseEntity<ErrorMessage> handleEventDeletionException(EventDeletionException exception,
                                                                      HttpServletRequest request) {
        printAPIError(exception.getMessage());

        HttpStatus ht = HttpStatus.CONFLICT;

        return ResponseEntity
                .status(ht)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, ht, exception.getMessage()));
    }

    private void printAPIError(String message) {
        log.error("API Error: {}", message);
    }
}

