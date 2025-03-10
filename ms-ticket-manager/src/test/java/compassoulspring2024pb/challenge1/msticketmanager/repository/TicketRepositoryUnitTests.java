package compassoulspring2024pb.challenge1.msticketmanager.repository;

import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.model.enums.TicketStatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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

        // Act
        Ticket foundTicket = ticketRepository.findActiveByCpf(ticket.getCpf()).orElse(null);

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

        // Act & Assert
        Optional<Ticket> foundTicket = ticketRepository.findActiveByCpf(ticket.getCpf());
        assertTrue(foundTicket.isEmpty());
    }

    @Test
    public void findActiveByCpf_withInvalidCpf_shouldReturnNull() {
        // Arrange
        String invalidCpf = "123.456.789-00";

        // Act
        Ticket foundTicket = ticketRepository.findActiveByCpf(invalidCpf).orElse(null);

        // Assert
        assertNull(foundTicket);
    }
}
