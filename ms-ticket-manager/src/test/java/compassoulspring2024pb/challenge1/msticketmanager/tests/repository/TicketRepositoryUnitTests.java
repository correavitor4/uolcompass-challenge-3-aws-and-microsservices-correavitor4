package compassoulspring2024pb.challenge1.msticketmanager.tests.repository;

import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.model.enums.TicketStatusEnum;
import compassoulspring2024pb.challenge1.msticketmanager.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TicketRepositoryUnitTests {

    @Autowired
    private TicketRepository ticketRepository;


    @BeforeEach
    public void setup() {
        ticketRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        ticketRepository.deleteAll();
    }

    @Test
    public void saveTicket_withValidTicket_shouldSaveTicket() {
        // Arrange
        Ticket ticket = Ticket
                .builder()
                .id(UUID.randomUUID())
                .cpf("123.456.789-00")
                .customerName("John Doe")
                .customerEmail("4k4lG@example.com")
                .eventId(UUID.randomUUID())
                .totalAmountBRL(BigDecimal.valueOf(100))
                .build();

        // Act
        Ticket savedTicket = ticketRepository.save(ticket);

        // Assert
        Ticket foundTicket = ticketRepository.findById(savedTicket.getId()).orElse(null);
        assertNotNull(foundTicket);
        assertEquals(ticket, foundTicket);
        assertEquals(savedTicket.getId(), foundTicket.getId());
        assertEquals(savedTicket.getCpf(), foundTicket.getCpf());
        assertEquals(savedTicket.getCustomerName(), foundTicket.getCustomerName());
        assertEquals(savedTicket.getCustomerEmail(), foundTicket.getCustomerEmail());
        assertEquals(savedTicket.getEventId(), foundTicket.getEventId());
        assertEquals(savedTicket.getTotalAmountBRL(), foundTicket.getTotalAmountBRL());
    }

    @Test
    public void findActiveById_withValidId_shouldReturnTicket() {
        // Arrange
        Ticket ticket = Ticket
                .builder()
                .id(UUID.randomUUID())
                .cpf("123.456.789-00")
                .customerName("John Doe")
                .customerEmail("4k4lG@example.com")
                .eventId(UUID.randomUUID())
                .totalAmountBRL(BigDecimal.valueOf(100))
                .build();
        UUID savedTicketId = ticketRepository.save(ticket).getId();

        // Act
        Ticket foundTicket = ticketRepository.findActiveById(savedTicketId).orElse(null);

        // Assert
        assertNotNull(foundTicket);
        assertEquals(ticket, foundTicket);
        assertEquals(savedTicketId, foundTicket.getId());
        assertEquals(ticket.getCpf(), foundTicket.getCpf());
        assertEquals(ticket.getCustomerName(), foundTicket.getCustomerName());
        assertEquals(ticket.getCustomerEmail(), foundTicket.getCustomerEmail());
        assertEquals(ticket.getEventId(), foundTicket.getEventId());
        assertEquals(ticket.getTotalAmountBRL(), foundTicket.getTotalAmountBRL());
    }

    @Test
    public void findActiveById_WithCancelledTicket_ShouldThrowException() {
        // Arrange
        Ticket ticket = Ticket
                .builder()
                .id(UUID.randomUUID())
                .cpf("123.456.789-00")
                .customerName("John Doe")
                .customerEmail("4k4lG@example.com")
                .eventId(UUID.randomUUID())
                .totalAmountBRL(BigDecimal.valueOf(100))
                .cancelledAt(Instant.now())
                .status(TicketStatusEnum.CANCELLED)
                .build();

        UUID savedTicketId = ticketRepository.save(ticket).getId();

        // Act & Assert
        Optional<Ticket> foundTicket = ticketRepository.findActiveById(savedTicketId);
        assertTrue(foundTicket.isEmpty());
    }

    @Test
    public void findActiveById_withInvalidId_shouldReturnNull() {
        // Arrange
        UUID invalidId = UUID.randomUUID();

        // Act
        Ticket foundTicket = ticketRepository.findActiveById(invalidId).orElse(null);

        // Assert
        assertNull(foundTicket);
    }

    @Test
    public void findActiveByCpf_withValidCpf_shouldReturnTicket() {
        // Arrange
        Ticket ticket = Ticket
                .builder()
                .id(UUID.randomUUID())
                .cpf("123.456.789-00")
                .customerName("John Doe")
                .customerEmail("4k4lG@example.com")
                .eventId(UUID.randomUUID())
                .totalAmountBRL(BigDecimal.valueOf(100))
                .build();
        UUID savedTicketId = ticketRepository.save(ticket).getId();

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Ticket> foundTickets = ticketRepository.findActiveByCpf(ticket.getCpf(), pageable);

        // Assert
        foundTickets.forEach(t -> {
            assertNotNull(t);
            assertEquals(ticket, t);
            assertEquals(savedTicketId, t.getId());
            assertEquals(ticket.getCpf(), t.getCpf());
            assertEquals(ticket.getCustomerName(), t.getCustomerName());
            assertEquals(ticket.getCustomerEmail(), t.getCustomerEmail());
            assertEquals(ticket.getEventId(), t.getEventId());
            assertEquals(ticket.getTotalAmountBRL(), t.getTotalAmountBRL());
        });
    }

    @Test
    public void findActiveByCpf_WithCancelledTicket_ShouldThrowException() {
        // Arrange
        Ticket ticket = Ticket
                .builder()
                .id(UUID.randomUUID())
                .cpf("123.456.789-00")
                .customerName("John Doe")
                .customerEmail("4k4lG@example.com")
                .eventId(UUID.randomUUID())
                .totalAmountBRL(BigDecimal.valueOf(100))
                .cancelledAt(Instant.now())
                .status(TicketStatusEnum.CANCELLED)
                .build();

        ticketRepository.save(ticket);

        Pageable pageable = PageRequest.of(0, 10);

        // Act & Assert
        Page<Ticket> foundTicket = ticketRepository.findActiveByCpf(ticket.getCpf(), pageable);
        assertTrue(foundTicket.isEmpty());
    }

    @Test
    public void findActiveByCpf_withInvalidCpf_shouldReturnEmptyList() {
        // Arrange
        String invalidCpf = "123.456.789-00";
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Ticket> foundTickets = ticketRepository.findActiveByCpf(invalidCpf, pageable);

        // Assert
        assertEquals(0, foundTickets.getTotalElements());
        assertEquals(0, foundTickets.getTotalPages());
        assertEquals(0, foundTickets.getContent().size());
    }

    @Test
    public void existsByEventId_withValidEventIdAndOneTicket_shouldReturnTrue() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        Ticket ticket = getTicketListWithProvidedEventId(eventId, 1).get(0);

        ticketRepository.save(ticket);

        // Act
        boolean result = ticketRepository.existsByEventId(eventId);

        // Assert
        assertTrue(result);
    }

    @Test
    public void existsByEventId_withValidEventIdAndNoTickets_shouldReturnFalse() {
        // Arrange
        UUID eventId = UUID.randomUUID();

        // Act
        boolean result = ticketRepository.existsByEventId(eventId);

        // Assert
        assertFalse(result);
    }

    @Test
    public void existsById_withMultipleTickets_shouldReturnTrue() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        List<Ticket> tickets = getTicketListWithProvidedEventId(eventId, 10);

        ticketRepository.saveAll(tickets);

        // Act
        boolean result = ticketRepository.existsByEventId(eventId);

        // Assert
        assertTrue(result);
    }

    private List<Ticket> getTicketListWithProvidedEventId(UUID eventId, int listSize) {
        return Stream
                .iterate(0, i -> i + 1)
                .limit(listSize)
                .map(i -> Ticket.builder()
                        .id(UUID.randomUUID())
                        .cpf("293.666.570-10")
                        .customerName("John Doe")
                        .customerEmail("4k4lG@example.com")
                        .eventId(eventId)
                        .totalAmountBRL(BigDecimal.valueOf(100))
                        .build())
                .collect(Collectors.toList());

    }
}
