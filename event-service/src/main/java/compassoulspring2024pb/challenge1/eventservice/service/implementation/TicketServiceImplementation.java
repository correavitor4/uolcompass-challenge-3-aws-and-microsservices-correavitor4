package compassoulspring2024pb.challenge1.eventservice.service.implementation;

import compassoulspring2024pb.challenge1.eventservice.integration.ticketsmaganer.TicketManagerClient;
import compassoulspring2024pb.challenge1.eventservice.service.definition.TicketService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImplementation implements TicketService {

    private final TicketManagerClient ticketManagerClient;

    @Override
    public boolean hasSoldTickets(UUID eventId) {
        try {
            ticketManagerClient.getByEventId(eventId);
            return true;
        } catch (FeignException e) {
            if (e instanceof FeignException.NotFound) return false;
            else throw new RuntimeException(e);
        }
    }
}
