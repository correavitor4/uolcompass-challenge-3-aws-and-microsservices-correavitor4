package compassoulspring2024pb.challenge1.eventservice.service.definition;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.service.dto.CreateEventInternalDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    Event create(CreateEventInternalDTO event);

    Event update(UpdateEventRequestDTO event, UUID id);

    Event findById(UUID id);
    Page<Event> findAll(Pageable pageable);

    void delete(UUID id);
}
