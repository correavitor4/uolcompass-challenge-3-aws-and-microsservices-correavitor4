package compassoulspring2024pb.challenge1.eventservice.service.definition;

import java.util.UUID;

public interface TicketService {
    boolean hasSoldTickets(UUID eventId);
}
