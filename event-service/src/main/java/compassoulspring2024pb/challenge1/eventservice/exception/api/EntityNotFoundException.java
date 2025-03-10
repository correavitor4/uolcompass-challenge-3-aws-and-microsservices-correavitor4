package compassoulspring2024pb.challenge1.eventservice.exception.api;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
