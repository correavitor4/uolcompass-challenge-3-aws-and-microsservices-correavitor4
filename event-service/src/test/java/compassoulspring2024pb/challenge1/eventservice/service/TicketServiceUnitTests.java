package compassoulspring2024pb.challenge1.eventservice.service;

import compassoulspring2024pb.challenge1.eventservice.integration.ticketsmaganer.TicketManagerClient;
import compassoulspring2024pb.challenge1.eventservice.service.definition.TicketService;
import compassoulspring2024pb.challenge1.eventservice.service.implementation.TicketServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceUnitTests {
    @Mock
    private TicketManagerClient ticketManagerClient;

    private TicketService ticketService;

    @BeforeEach
    public void setup() {
        ticketService = new TicketServiceImplementation(ticketManagerClient);
    }

    @Test
    public void hasSoldTickets_withNoTickets_shouldReturnFalse() {
        UUID eventId = UUID.randomUUID();
        when(ticketManagerClient.getByEventId(eventId)).thenReturn(false);
        Assertions.assertFalse(ticketService.hasSoldTickets(eventId));
    }

    @Test
    public void hasSoldTickets_withTickets_shouldReturnTrue() {
        UUID eventId = UUID.randomUUID();
        when(ticketManagerClient.getByEventId(eventId)).thenReturn(true);
        Assertions.assertTrue(ticketService.hasSoldTickets(eventId));
    }

    @Test
    public void hasSoldTickets_withException_shouldThrowException() {
        UUID eventId = UUID.randomUUID();
        when(ticketManagerClient.getByEventId(eventId)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> ticketService.hasSoldTickets(eventId));
    }
}
