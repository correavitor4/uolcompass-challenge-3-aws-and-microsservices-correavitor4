package compassoulspring2024pb.challenge1.msticketmanager.tests.service;

import compassoulspring2024pb.challenge1.msticketmanager.exception.EntityNotFoundException;
import compassoulspring2024pb.challenge1.msticketmanager.exception.NonExistentEventIdProvidedException;
import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.model.enums.TicketStatusEnum;
import compassoulspring2024pb.challenge1.msticketmanager.repository.TicketRepository;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.EventService;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.TicketService;
import compassoulspring2024pb.challenge1.msticketmanager.service.implementation.TicketServiceImplementation;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTests {
    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventService eventService;

    private TicketService ticketService;

    @BeforeEach
    public void setup() {
        ticketService = new TicketServiceImplementation(eventService, ticketRepository);
    }

    @Test
    public void create_withValidTicket_shouldReturnTicket() {
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

        when(eventService.existsById(any(UUID.class))).thenReturn(true);
        when(ticketRepository.save(any())).thenReturn(ticket);

        ModelMapper modelMapper = new ModelMapper();
        CreateTicketRequestDTO createTicketRequestDTO = modelMapper.map(ticket, CreateTicketRequestDTO.class);

        // Act
        Ticket result = ticketService.createTicket(createTicketRequestDTO);

        // Assert
        assertEquals(ticket, result);
    }

    @Test
    public void create_withNonExistentEventId_shouldThrowException() {
        // Arrange
        CreateTicketRequestDTO createTicketRequestDTO = new CreateTicketRequestDTO(
                "123.456.789-00",
                "John Doe",
                "4k4lG@example.com",
                UUID.randomUUID(),
                BigDecimal.valueOf(100)
        );

        // Act & Assert
        when(eventService.existsById(any(UUID.class))).thenReturn(false);
        assertThrows(NonExistentEventIdProvidedException.class, () -> ticketService.createTicket(createTicketRequestDTO));
    }

    @Test
    public void findById_withValidId_shouldReturnTicket() {
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

        when(ticketRepository.findActiveById(any(UUID.class))).thenReturn(Optional.of(ticket));

        // Act
        Ticket foundTicket = ticketService.findById(ticket.getId());

        // Assert
        assertNotNull(foundTicket);
        assertEquals(ticket, foundTicket);
        assertEquals(ticket.getId(), foundTicket.getId());
        assertEquals(ticket.getCpf(), foundTicket.getCpf());
        assertEquals(ticket.getCustomerName(), foundTicket.getCustomerName());
        assertEquals(ticket.getCustomerEmail(), foundTicket.getCustomerEmail());
        assertEquals(ticket.getEventId(), foundTicket.getEventId());
        assertEquals(ticket.getTotalAmountBRL(), foundTicket.getTotalAmountBRL());
    }

    @Test
    public void findById_withNonExistentId_shouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        when(ticketRepository.findActiveById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.findById(id));
    }

    @Test
    public void findByCpf_withValidCpf_shouldReturnTicket() {
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

        when(ticketRepository.findActiveByCpf(any(String.class))).thenReturn(Optional.of(ticket));

        // Act
        Ticket foundTicket = ticketService.findByCpf(ticket.getCpf());

        // Assert
        assertNotNull(foundTicket);
        assertEquals(ticket, foundTicket);
        assertEquals(ticket.getId(), foundTicket.getId());
        assertEquals(ticket.getCpf(), foundTicket.getCpf());
        assertEquals(ticket.getCustomerName(), foundTicket.getCustomerName());
        assertEquals(ticket.getCustomerEmail(), foundTicket.getCustomerEmail());
        assertEquals(ticket.getEventId(), foundTicket.getEventId());
        assertEquals(ticket.getTotalAmountBRL(), foundTicket.getTotalAmountBRL());
    }

    @Test
    public void findByCpf_withNonExistentCpf_shouldThrowException() {
        // Arrange
        String cpf = "123.456.789-00";

        // Act & Assert
        when(ticketRepository.findActiveByCpf(any(String.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.findByCpf(cpf));
    }

    @Test
    public void delete_withValidId_shouldDeleteTicket() {
        // Arrange
        UUID id = UUID.randomUUID();

        Ticket ticket = mock(Ticket.class);

        when(ticketRepository.findActiveById(any(UUID.class))).thenReturn(Optional.of(ticket));

        // Act
        ticketService.deleteTicket(id);

        // Assert
        verify(ticket).setCancelledAt(any(Instant.class));
        verify(ticket).setStatus(TicketStatusEnum.CANCELLED);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void delete_withNonExistentId_shouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act & Assert
        when(ticketRepository.findActiveById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.deleteTicket(id));
    }

    @Test
    public void findAll_withValidPageable_shouldReturnPageOfTickets() {
        // Arrange
        Pageable pageable = Pageable.ofSize(10);
        Page<Ticket> ticketsPage = new PageImpl<>(Collections.emptyList());

        when(ticketRepository.findAllActive(pageable)).thenReturn(ticketsPage);

        // Act
        Page<Ticket> result = ticketService.findAll(pageable);

        // Assert
        assertEquals(ticketsPage, result);
    }

    @Test
    public void existsByEventId_withValidEventIdAndNoTickets_shouldReturnFalse() {
        // Arrange
        UUID eventId = UUID.randomUUID();

        // Act
        boolean result = ticketService.existsByEventId(eventId);

        // Assert
        assertFalse(result);
    }

    @Test
    public void existsByEventId_withValidEventIdAndOneTicket_shouldReturnTrue() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        when(ticketRepository.existsByEventId(eventId)).thenReturn(true);

        // Act
        boolean result = ticketService.existsByEventId(eventId);

        // Assert
        assertTrue(result);
    }
}
