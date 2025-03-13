package compassoulspring2024pb.challenge1.eventservice.web.api.v1.controller;

import compassoulspring2024pb.challenge1.eventservice.exception.api.message.ErrorMessage;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto.ViaCepResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.service.definition.AddressService;
import compassoulspring2024pb.challenge1.eventservice.service.definition.EventService;
import compassoulspring2024pb.challenge1.eventservice.service.dto.CreateEventInternalDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.EventResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventRequestDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.utils.URIUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Event", description = "Event API for events management")
public class EventController {

    private final AddressService addressService;
    private final EventService eventService;

    @Operation(
            summary = "Create a new event",
            description = "Create a new event with a valid dto",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EventResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping("/create")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid CreateEventDTO createEventDTO) {
        ViaCepResponseDTO address = addressService.findByCep(createEventDTO.getCep());

        CreateEventInternalDTO internalDTO = new CreateEventInternalDTO(createEventDTO, address);

        Event event = eventService.create(internalDTO);

        return ResponseEntity
                .created(URIUtils.generateResourceURI(event.getId()))
                .body(EventResponseDTO.fromModel(event));
    }

    @Operation(
            summary = "Find an event by id",
            description = "Find an event by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = EventResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable UUID id) {
        Event event = eventService.findById(id);

        return ResponseEntity.ok(EventResponseDTO.fromModel(event));
    }

    @Operation(
            summary = "Find all events",
            description = "Find all events",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventResponseDTO.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> findAll(Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);

        Page<EventResponseDTO> responseList = events.map(EventResponseDTO::fromModel);

        return ResponseEntity.ok(responseList);
    }

    @Operation(
        summary = "Update an event",
        description = "Update an event with a valid dto",
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = EventResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @RequestBody @Valid UpdateEventRequestDTO dto) {
        Event event = eventService.update(dto, id);

        return ResponseEntity.ok(EventResponseDTO.fromModel(event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
