package compassoulspring2024pb.challenge1.eventservice.repository;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class EventRepositoryUnitTests
{

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    public void setup(){
        eventRepository.deleteAll();
    }

    @BeforeEach
    public void cleanup(){
        eventRepository.deleteAll();
    }

    @Test
    public void saveEvent_withValidData_shouldReturnSavedEvent(){
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

        Assertions.assertNotNull(savedEvent);
        Assertions.assertEquals(event.getName(), savedEvent.getName());
        Assertions.assertEquals(event.getCep(), savedEvent.getCep());
        Assertions.assertEquals(event.getAddress(), savedEvent.getAddress());
        Assertions.assertEquals(event.getCity(), savedEvent.getCity());
        Assertions.assertEquals(event.getDistrict(), savedEvent.getDistrict());
        Assertions.assertEquals(event.getState(), savedEvent.getState());
    }

    @Test
    public void findActiveById_withValidId_shouldReturnEvent(){
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

        Assertions.assertNotNull(foundEvent);
        Assertions.assertEquals(event.getName(), foundEvent.getName());
        Assertions.assertEquals(event.getCep(), foundEvent.getCep());
        Assertions.assertEquals(event.getAddress(), foundEvent.getAddress());
        Assertions.assertEquals(event.getCity(), foundEvent.getCity());
        Assertions.assertEquals(event.getDistrict(), foundEvent.getDistrict());
        Assertions.assertEquals(event.getState(), foundEvent.getState());
    }

    @Test
    public void findById_withInvalidId_shouldReturnNull(){
        UUID invalidId = UUID.randomUUID();
        Event foundEvent = eventRepository.findActiveById(invalidId).orElse(null);
        Assertions.assertNull(foundEvent);
    }

    @Test
    public void findActiveById_withIdOfDeletedEvent_shouldReturnNull(){
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

        Assertions.assertNull(foundEvent);
    }

    @Test
    public void findAllActive_shouldReturnAllActiveEvents(){
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

        List<Event> activeEvents = eventRepository.findAllActive();

        Assertions.assertEquals(2, activeEvents.size());
        Assertions.assertTrue(activeEvents.containsAll(List.of(event1, event2)));
        Assertions.assertFalse(activeEvents.contains(event3));

        Assertions.assertEquals(3, eventRepository.count());
    }
}
