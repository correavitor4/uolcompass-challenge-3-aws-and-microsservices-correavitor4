package compassoulspring2024pb.challenge1.eventservice.service;

import compassoulspring2024pb.challenge1.eventservice.exception.api.EntityNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import compassoulspring2024pb.challenge1.eventservice.repository.EventRepository;
import compassoulspring2024pb.challenge1.eventservice.service.implementation.EventServiceImplementation;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.*;
import java.util.stream.Collectors;

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
        CreateEventDTO event = new CreateEventDTO("event1",
                "cep1",
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
        UpdateEventDTO updateEventDTO = new UpdateEventDTO("event1",
                "cep1",
                "address1",
                "city1",
                "district1",
                StatesEnum.SP);
        Event event = updateEventDTO.toModel();

        when(eventRepository.findActiveById(event.getId())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event result = eventService.update(updateEventDTO, event.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateEventDTO.getName(), result.getName());
        Assertions.assertEquals(updateEventDTO.getCep(), result.getCep());
        Assertions.assertEquals(updateEventDTO.getAddress(), result.getAddress());
        Assertions.assertEquals(updateEventDTO.getCity(), result.getCity());
        Assertions.assertEquals(updateEventDTO.getDistrict(), result.getDistrict());
        Assertions.assertEquals(updateEventDTO.getState(), result.getState());
    }

    @Test
    public void update_withInvalidId_shouldThrowEntityNotFoundException() {
        UpdateEventDTO updateEventDTO = new UpdateEventDTO("event1",
                "cep1",
                "address1",
                "city1",
                "district1",
                StatesEnum.SP);
        Event event = updateEventDTO.toModel();

        when(eventRepository.findActiveById(event.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> eventService.update(updateEventDTO, event.getId()));
    }

    @Test
    public void findById_withValidId_shouldReturnEvent() {
        Event event = new Event(
                "Test Event",
                "75000-000",
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
        Event event1 = new Event(
            "Test Event 1",
            "75000-000",
            "Test Address 1",
            "Test City 1",
            "Test District 1",
            StatesEnum.AC
        );

        Event event2 = new Event(
            "Test Event 2",
            "75000-000",
            "Test Address 2",
            "Test City 2",
            "Test District 2",
            StatesEnum.AC
        );

        Event event3 = new Event(
            "Test Event 3",
            "75000-000",
            "Test Address 3",
            "Test City 3",
            "Test District 3",
            StatesEnum.AC
        );

        List<Event> mockedListToReturn = List.of(event1,event2,event3);
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
}
