package compassoulspring2024pb.challenge1.eventservice.web.api.v1.controller;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.service.EventService;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.EventDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.utils.URIUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RestController("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestBody @Valid CreateEventDTO dto) {
        Event event = eventService.create(dto);

        return ResponseEntity
                .created(URIUtils.generateResourceURI(event.getId()))
                .body(EventDTO.fromModel(event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> findById(@PathVariable UUID id) {
        Event event = eventService.findById(id);

        return ResponseEntity.ok(EventDTO.fromModel(event));
    }

    @GetMapping
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);

        Page<EventDTO> responseList = events.map(EventDTO::fromModel);

        return ResponseEntity.ok(responseList);
    }
}
