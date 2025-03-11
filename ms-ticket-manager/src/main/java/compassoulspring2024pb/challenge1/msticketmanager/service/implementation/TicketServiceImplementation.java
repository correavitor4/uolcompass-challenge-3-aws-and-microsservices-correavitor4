package compassoulspring2024pb.challenge1.msticketmanager.service.implementation;

import compassoulspring2024pb.challenge1.msticketmanager.integration.event_manager.EventManagerClient;
import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.repository.TicketRepository;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.TicketService;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImplementation implements TicketService {
    private final EventManagerClient eventManagerClient;

    @Override
    public Ticket createTicket(CreateTicketRequestDTO dto) {
        retur
    }

    @Override
    public Ticket findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Ticket findByCpf(String cpf) {
        return Optional.empty();
    }

    @Override
    public Ticket deleteTicket(UUID id) {
        return Optional.empty();
    }

    @Override
    public Page<Ticket> findAll(Pageable pageable) {
        return null;
    }
}
