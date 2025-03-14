package compassoulspring2024pb.challenge1.msticketmanager.service.definition;

import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketService {
        Ticket createTicket(CreateTicketRequestDTO dto) ;
        Ticket findById(UUID id) ;
        Ticket findByCpf(String cpf) ;
        Ticket deleteTicket(UUID id) ;

        Page<Ticket> findAll(Pageable pageable);

        Boolean existsByEventId(UUID eventId);
}
