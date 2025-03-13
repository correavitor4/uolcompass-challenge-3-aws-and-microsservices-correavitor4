package compassoulspring2024pb.challenge1.eventservice.service.implementation;

import compassoulspring2024pb.challenge1.eventservice.exception.api.APIInternalServerErrorException;
import compassoulspring2024pb.challenge1.eventservice.exception.api.EntityNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.exception.api.EventDeletionException;
import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.repository.EventRepository;
import compassoulspring2024pb.challenge1.eventservice.service.definition.EventService;
import compassoulspring2024pb.challenge1.eventservice.service.definition.TicketService;
import compassoulspring2024pb.challenge1.eventservice.service.dto.CreateEventInternalDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImplementation implements EventService {

    private final EventRepository eventRepository;
    private final TicketService ticketService;

    @Override
    public Event create(CreateEventInternalDTO dto) {
        try {

            Event eventToSave = dto.toModel();
            return eventRepository.save(eventToSave);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new APIInternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public Event update(UpdateEventRequestDTO event, UUID id) throws EntityNotFoundException {
        try {
            Event savedEvent = findById(id);

            savedEvent.setName(event.getName());
            savedEvent.setCep(event.getCep());
            savedEvent.setAddress(event.getAddress());
            savedEvent.setCity(event.getCity());
            savedEvent.setDistrict(event.getDistrict());

            return eventRepository.save(savedEvent);

        } catch (EntityNotFoundException e) {
            String sb = "Event with id " + id + " not found" +
                    e.getMessage() +
                    Arrays.toString(e.getStackTrace());
            log.error(sb);

            throw new EntityNotFoundException("Event with id " + id + " not found");
        }
    }

    @Override
    public Event findById(UUID id) throws EntityNotFoundException {
        return eventRepository.findActiveById(id).orElseThrow(
                () -> new EntityNotFoundException("Event not found")
        );
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAllActive(pageable);
    }

    @Override
    public void delete(UUID id) {
        if(ticketService.hasSoldTickets(id)) throw new EventDeletionException("Cannot delete event with sold tickets");

        Event event = findById(id);
        event.setAsDeleted();
        eventRepository.save(event);
    }
}
