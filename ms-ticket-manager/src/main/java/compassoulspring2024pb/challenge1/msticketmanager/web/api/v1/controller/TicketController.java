package compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.controller;

import compassoulspring2024pb.challenge1.msticketmanager.exception.api.message.ErrorMessage;
import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.TicketService;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.controller.utils.URIUtils;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.TicketResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Tickets", description = "Endpoints to manage tickets")
public class TicketController {

    private final TicketService ticketService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Create a new ticket",
            description = "Create a new ticket. A valid and existent eventId" +
                    " must be provided",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Ticket created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<TicketResponseDTO> createTicket(@RequestBody @Valid CreateTicketRequestDTO dto) {
        Ticket ticket = ticketService.createTicket(dto);

        TicketResponseDTO response = modelMapper.map(ticket, TicketResponseDTO.class);

        URI resourceUri = URIUtils.generateResourceURI(ticket.getId());
        return ResponseEntity.created(resourceUri).body(response);
    }

    @Operation(
            summary = "Find a ticket by id",
            description = "Find a ticket by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ticket found successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ticket not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> findById(@PathVariable UUID id) {
        Ticket ticket = ticketService.findById(id);

        TicketResponseDTO response = modelMapper.map(ticket, TicketResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Find a ticket by cpf",
            description = "Find a ticket by cpf",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ticket found successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping("/findByCpf/{cpf}")
    public ResponseEntity<TicketResponseDTO> findByCpf(@PathVariable String cpf) {


        Ticket ticket = ticketService.findByCpf(cpf);

        TicketResponseDTO response = modelMapper.map(ticket, TicketResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Find all tickets",
            description = "Find all tickets",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tickets found successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<Page<TicketResponseDTO>> findAll(Pageable pageable) {
        Page<Ticket> tickets = ticketService.findAll(pageable);

        Page<TicketResponseDTO> response = tickets.map(ticket -> modelMapper.map(ticket, TicketResponseDTO.class));
        return ResponseEntity.ok(response);
    }
}
