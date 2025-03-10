package compassoulspring2024pb.challenge1.eventservice.exception.api;

public class APIInternalServerErrorException extends RuntimeException {
    public APIInternalServerErrorException(String message) {
        super(message);
    }
}
