package compassoulspring2024pb.challenge1.eventservice.service.implementation;

import compassoulspring2024pb.challenge1.eventservice.exception.api.APIInternalServerErrorException;
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
            return ticketManagerClient.getByEventId(eventId);
        } catch (FeignException e) {
            throw new APIInternalServerErrorException(e.getMessage());
        }
    }
}
