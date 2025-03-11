package compassoulspring2024pb.challenge1.msticketmanager.exception;

import lombok.Getter;

import java.util.UUID;

public class InexistentEventIdProvidedException extends RuntimeException {

    @Getter
    private UUID eventId;

    public InexistentEventIdProvidedException(String message, UUID eventId) {
        super(message);
    }
}
