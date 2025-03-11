package compassoulspring2024pb.challenge1.msticketmanager.exception;

import lombok.Getter;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {


    public EntityNotFoundException(String message) {
        super(message);
    }
}
