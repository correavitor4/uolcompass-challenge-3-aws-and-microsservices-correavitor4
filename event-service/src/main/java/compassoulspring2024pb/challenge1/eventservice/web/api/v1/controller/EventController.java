package compassoulspring2024pb.challenge1.eventservice.web.api.v1.controller;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.service.EventService;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventRequestDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.EventResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventRequestDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.utils.URIUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

@RestController("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody @Valid CreateEventRequestDTO dto) {
        Event event = eventService.create(dto);

        return ResponseEntity
                .created(URIUtils.generateResourceURI(event.getId()))
                .body(EventResponseDTO.fromModel(event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findById(@PathVariable UUID id) {
        Event event = eventService.findById(id);

        return ResponseEntity.ok(EventResponseDTO.fromModel(event));
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> findAll(Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);

        Page<EventResponseDTO> responseList = events.map(EventResponseDTO::fromModel);

        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable UUID id, @RequestBody @Valid UpdateEventRequestDTO dto) {
        Event event = eventService.update(dto, id);

        return ResponseEntity.ok(EventResponseDTO.fromModel(event));
    }
}
