package compassoulspring2024pb.challenge1.eventservice.service;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.web.dto.CreateEventDTO;
import compassoulspring2024pb.challenge1.eventservice.web.dto.UpdateEventDTO;

import java.util.List;
import java.util.UUID;

public interface EventService {
    Event create(CreateEventDTO event);

    Event update(UpdateEventDTO event, UUID id);

    Event findById(UUID id);
    List<Event> findAll();
}
