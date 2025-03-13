package compassoulspring2024pb.challenge1.msticketmanager.exception.handler;

import compassoulspring2024pb.challenge1.msticketmanager.exception.EntityNotFoundException;
import compassoulspring2024pb.challenge1.msticketmanager.exception.NonExistentEventIdProvidedException;
import compassoulspring2024pb.challenge1.msticketmanager.exception.api.ApiInternalServerException;
import compassoulspring2024pb.challenge1.msticketmanager.exception.api.message.ErrorMessage;
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

    @ExceptionHandler(NonExistentEventIdProvidedException.class)
    public ResponseEntity<ErrorMessage> handleNonExistentEventIdProvidedException(NonExistentEventIdProvidedException exception,
                                                                                   HttpServletRequest request) {
        printAPIError(exception.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, status, exception.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception,
                                                                      HttpServletRequest request) {
        printAPIError(exception.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, status, exception.getMessage()));
    }

    @ExceptionHandler(ApiInternalServerException.class)
    public ResponseEntity<ErrorMessage> handleApiInternalServerException(ApiInternalServerException exception,
                                                                          HttpServletRequest request) {
        printAPIError(exception.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, status, "An error occurred during the request processing"));
    }

    private void printAPIError(String message) {
        log.error("API Error: {}", message);
    }
}
