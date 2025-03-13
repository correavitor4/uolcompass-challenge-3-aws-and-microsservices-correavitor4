package compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.controller;

import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.TicketService;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.controller.utils.URIUtils;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.TicketResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody @Valid CreateTicketRequestDTO dto) {
        Ticket ticket = ticketService.createTicket(dto);

        TicketResponseDTO response = modelMapper.map(ticket, TicketResponseDTO.class);

        URI resourceUri = URIUtils.generateResourceURI(ticket.getId());
        return ResponseEntity.created(resourceUri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> findById(@PathVariable UUID id) {
        Ticket ticket = ticketService.findById(id);

        TicketResponseDTO response = modelMapper.map(ticket, TicketResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByCpf/{cpf}")
    public ResponseEntity<TicketResponseDTO> findByCpf(@PathVariable @CPF @Valid String cpf) {
        Ticket ticket = ticketService.findByCpf(cpf);

        TicketResponseDTO response = modelMapper.map(ticket, TicketResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<Page<TicketResponseDTO>> findAll(Pageable pageable) {
        Page<Ticket> tickets = ticketService.findAll(pageable);

        Page<TicketResponseDTO> response = tickets.map(ticket -> modelMapper.map(ticket, TicketResponseDTO.class));
        return ResponseEntity.ok(response);
    }
}
