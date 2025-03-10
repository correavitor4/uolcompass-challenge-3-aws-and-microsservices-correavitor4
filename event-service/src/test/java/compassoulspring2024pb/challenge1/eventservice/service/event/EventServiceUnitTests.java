package compassoulspring2024pb.challenge1.eventservice.service.event;

import compassoulspring2024pb.challenge1.eventservice.exception.api.EntityNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import compassoulspring2024pb.challenge1.eventservice.repository.EventRepository;
import compassoulspring2024pb.challenge1.eventservice.service.definition.EventService;
import compassoulspring2024pb.challenge1.eventservice.service.implementation.EventServiceImplementation;
import compassoulspring2024pb.challenge1.eventservice.service.dto.CreateEventInternalDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTests {
    @Mock
    private EventRepository eventRepository;

    private EventService eventService;

    @BeforeEach
    public void setup() {
        eventService = new EventServiceImplementation(eventRepository);
    }

    @Test
    public void create_withValidEvent_shouldReturnEvent() {
        CreateEventInternalDTO event = new CreateEventInternalDTO("event1",
                "cep1",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS),
                "address1",
                "city1",
                "district1",
                StatesEnum.SP);

        when(eventRepository.save(any(Event.class))).thenReturn(event.toModel());
        Event result = eventService.create(event);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(event.getName(), result.getName());
        Assertions.assertEquals(event.getCep(), result.getCep());
        Assertions.assertEquals(event.getAddress(), result.getAddress());
        Assertions.assertEquals(event.getCity(), result.getCity());
        Assertions.assertEquals(event.getDistrict(), result.getDistrict());
        Assertions.assertEquals(event.getState(), result.getState());
    }

    @Test
    public void update_withValidData_shouldReturnEvent() {
        UpdateEventRequestDTO updateEventInternalDTO = new UpdateEventRequestDTO("event1",
                "cep1",
                "address1",
                "city1",
                "district1",
                StatesEnum.SP);
        Event event = new Event(
                updateEventInternalDTO.getName(),
                updateEventInternalDTO.getCep(),
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS),
                updateEventInternalDTO.getAddress(),
                updateEventInternalDTO.getCity(),
                updateEventInternalDTO.getDistrict(),
                updateEventInternalDTO.getState());

        when(eventRepository.findActiveById(event.getId())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.update(updateEventInternalDTO, event.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateEventInternalDTO.getName(), result.getName());
        Assertions.assertEquals(updateEventInternalDTO.getCep(), result.getCep());
        Assertions.assertEquals(updateEventInternalDTO.getAddress(), result.getAddress());
        Assertions.assertEquals(updateEventInternalDTO.getCity(), result.getCity());
        Assertions.assertEquals(updateEventInternalDTO.getDistrict(), result.getDistrict());
        Assertions.assertEquals(updateEventInternalDTO.getState(), result.getState());
    }

    @Test
    public void update_withInvalidId_shouldThrowEntityNotFoundException() {
        UpdateEventRequestDTO updateEventInternalDTO = new UpdateEventRequestDTO("event1",
                "cep1",
                "address1",
                "city1",
                "district1",
                StatesEnum.SP);
        Event event = new Event(
                updateEventInternalDTO.getName(),
                updateEventInternalDTO.getCep(),
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS),
                updateEventInternalDTO.getAddress(),
                updateEventInternalDTO.getCity(),
                updateEventInternalDTO.getDistrict(),
                updateEventInternalDTO.getState()
        );

        when(eventRepository.findActiveById(event.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> eventService.update(updateEventInternalDTO, event.getId()));
    }

    @Test
    public void findById_withValidId_shouldReturnEvent() {
        Event event = new Event(
                "Test Event",
                "75000-000",
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS),
                "Test Address",
                "Test City",
                "Test District",
                StatesEnum.AC
        );

        when(eventRepository.findActiveById(event.getId())).thenReturn(Optional.of(event));
        Event result = eventService.findById(event.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(event.getName(), result.getName());
        Assertions.assertEquals(event.getCep(), result.getCep());
        Assertions.assertEquals(event.getAddress(), result.getAddress());
        Assertions.assertEquals(event.getCity(), result.getCity());
        Assertions.assertEquals(event.getDistrict(), result.getDistrict());
        Assertions.assertEquals(event.getState(), result.getState());
    }

    @Test
    public void findById_withInvalidId_shouldThrowEntityNotFoundException() {
        UUID invalidId = UUID.randomUUID();
        when(eventRepository.findActiveById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> eventService.findById(invalidId));
    }

    @Test
    public void findAll_withNoEvents_shouldReturnEmptyList() {
        when(eventRepository.findAllActive(any(Pageable.class))).thenReturn(Page.empty());

        Assertions.assertEquals(0, eventService.findAll(Pageable.ofSize(10)).getContent().size());
    }

    @Test
    public void findAll_withEvents_shouldReturnAllEvents() {
        List<Event> mockedListToReturn = getEvents();
        Page<Event> mockedPageToReturn = new PageImpl<>(mockedListToReturn);

        when(eventRepository.findAllActive(any(Pageable.class))).thenReturn(mockedPageToReturn);

        Page<Event> result = eventService.findAll(Pageable.ofSize(10));

        Assertions.assertEquals(mockedListToReturn.size(), result.getContent().size());

        result.forEach(event -> {
            Assertions.assertTrue(mockedPageToReturn.getContent().contains(event));
            Assertions.assertNotNull(event.getId());
            Assertions.assertNotNull(event.getName());
            Assertions.assertNotNull(event.getCep());
            Assertions.assertNotNull(event.getAddress());
            Assertions.assertNotNull(event.getCity());
            Assertions.assertNotNull(event.getDistrict());
            Assertions.assertNotNull(event.getState());
        });
    }

    private static List<Event> getEvents() {
        Event event1 = new Event(
                "Test Event 1",
                "75000-000",
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS),
                "Test Address 1",
                "Test City 1",
                "Test District 1",
                StatesEnum.AC
        );

        Event event2 = new Event(
                "Test Event 2",
                "75000-000",
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS),
                "Test Address 2",
                "Test City 2",
                "Test District 2",
                StatesEnum.AC
        );

        Event event3 = new Event(
                "Test Event 3",
                "75000-000",
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS),
                "Test Address 3",
                "Test City 3",
                "Test District 3",
                StatesEnum.AC
        );

        return List.of(event1, event2, event3);
    }
}
