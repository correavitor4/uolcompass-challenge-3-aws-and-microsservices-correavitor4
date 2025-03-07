package compassoulspring2024pb.challenge1.eventservice.repository;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

//TODO: write tests for pageable
@SpringBootTest
public class EventRepositoryUnitTests {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @BeforeEach
    public void cleanup() {
        eventRepository.deleteAll();
    }

    @Test
    public void saveEvent_withValidData_shouldReturnSavedEvent() {
        Event event = new Event(
                "Test Event",
                "75000-000",
                "Test Address",
                "Test City",
                "Test District",
                StatesEnum.AC
        );

        UUID savedEventId = eventRepository.save(event).getId();

        Event savedEvent = eventRepository.findActiveById(savedEventId).orElse(null);

        assertNotNull(savedEvent);
        assertEquals(event.getName(), savedEvent.getName());
        assertEquals(event.getCep(), savedEvent.getCep());
        assertEquals(event.getAddress(), savedEvent.getAddress());
        assertEquals(event.getCity(), savedEvent.getCity());
        assertEquals(event.getDistrict(), savedEvent.getDistrict());
        assertEquals(event.getState(), savedEvent.getState());
    }

    @Test
    public void findActiveById_withValidId_shouldReturnEvent() {
        Event event = new Event(
                "Test Event",
                "75000-000",
                "Test Address",
                "Test City",
                "Test District",
                StatesEnum.AC
        );

        UUID savedEventId = eventRepository.save(event).getId();

        Event foundEvent = eventRepository.findActiveById(savedEventId).orElse(null);

        assertNotNull(foundEvent);
        assertEquals(event.getName(), foundEvent.getName());
        assertEquals(event.getCep(), foundEvent.getCep());
        assertEquals(event.getAddress(), foundEvent.getAddress());
        assertEquals(event.getCity(), foundEvent.getCity());
        assertEquals(event.getDistrict(), foundEvent.getDistrict());
        assertEquals(event.getState(), foundEvent.getState());
    }

    @Test
    public void findById_withInvalidId_shouldReturnNull() {
        UUID invalidId = UUID.randomUUID();
        Event foundEvent = eventRepository.findActiveById(invalidId).orElse(null);
        assertNull(foundEvent);
    }

    @Test
    public void findActiveById_withIdOfDeletedEvent_shouldReturnNull() {
        Event event = new Event(
                "Test Event",
                "75000-000",
                "Test Address",
                "Test City",
                "Test District",
                StatesEnum.AC
        );

        event.setAsDeleted();

        UUID savedEventId = eventRepository.save(event).getId();

        Event foundEvent = eventRepository.findActiveById(savedEventId).orElse(null);

        assertNull(foundEvent);
    }

    @Test
    public void findAllActive_shouldReturnAllActiveEvents() {
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
        event3.setAsDeleted();

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        Pageable pageable = PageRequest.of(0, 3);
        Page<Event> activeEvents = eventRepository.findAllActive(pageable);

        assertEquals(2, activeEvents.getContent().size());
        assertTrue(activeEvents.getContent().containsAll(List.of(event1, event2)));
        assertFalse(activeEvents.getContent().contains(event3));

        assertEquals(3, eventRepository.count());
    }
}
