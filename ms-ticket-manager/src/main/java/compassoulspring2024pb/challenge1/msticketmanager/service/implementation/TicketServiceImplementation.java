package compassoulspring2024pb.challenge1.msticketmanager.service.implementation;

import compassoulspring2024pb.challenge1.msticketmanager.exception.EntityNotFoundException;
import compassoulspring2024pb.challenge1.msticketmanager.exception.NonExistentEventIdProvidedException;
import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.model.enums.TicketStatusEnum;
import compassoulspring2024pb.challenge1.msticketmanager.repository.TicketRepository;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.EventService;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.TicketService;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImplementation implements TicketService {

    private final EventService eventService;

    private final TicketRepository ticketRepository;

    @Override
    public Ticket createTicket(CreateTicketRequestDTO dto) {
        if (!eventService.existsById(dto.getEventId()))
            throw new NonExistentEventIdProvidedException("Event with id " + dto.getEventId() + " not found");

        ModelMapper modelMapper = new ModelMapper();

        return ticketRepository.save(modelMapper.map(dto, Ticket.class));
    }

    @Override
    public Ticket findById(UUID id) {
        return ticketRepository
                .findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with id " + id + " not found"));
    }

    @Override
    public Ticket findByCpf(String cpf) {
        return ticketRepository
                .findActiveByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Ticket with cpf " + cpf + " not found"));

    }

    @Override
    public Ticket deleteTicket(UUID id) {
        Ticket ticket = findById(id);

        ticket.setCancelledAt(Instant.now());
        ticket.setStatus(TicketStatusEnum.CANCELLED);

        return ticketRepository.save(ticket);
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {

        return ticketRepository.findAllActive(pageable);
    }

    @Override
    public Boolean areThereTicketsByEventId(UUID eventId) {
        return ticketRepository.existsByEventId(eventId);
    }
}
