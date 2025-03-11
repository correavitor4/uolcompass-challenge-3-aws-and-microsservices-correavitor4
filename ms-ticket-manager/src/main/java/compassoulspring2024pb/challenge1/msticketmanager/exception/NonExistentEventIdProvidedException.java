package compassoulspring2024pb.challenge1.msticketmanager.exception;

public class NonExistentEventIdProvidedException extends RuntimeException {

    public NonExistentEventIdProvidedException(String message) {
        super(message);
    }
}
