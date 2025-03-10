package compassoulspring2024pb.challenge1.eventservice.web.api.v1.controller;

import compassoulspring2024pb.challenge1.eventservice.exception.api.message.ErrorMessage;
import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import compassoulspring2024pb.challenge1.eventservice.repository.EventRepository;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.EventResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.UpdateEventRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerIntegrationTests {
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private EventRepository eventRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        eventRepository.deleteAll();
    }

    @Test
    public void create_withValidDate_shouldReturn201andLocationHeaderAndBody() {
        CreateEventDTO dto = new CreateEventDTO(
                "Event 1",
                "89025-302",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS));

        String baseUrl = "http://localhost:" + port + "/api/v1/events";
        ResponseEntity<EventResponseDTO> response = restTemplate.postForEntity(baseUrl + "/create", dto, EventResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(dto.getName(), response.getBody().getName());
        assertEquals(dto.getCep(), response.getBody().getCep());
        assertNotNull(response.getBody().getAddress());
        assertNotNull(response.getBody().getCity());
        assertNotNull(response.getBody().getDistrict());
        assertNotNull(response.getBody().getState());
    }

    @Test
    public void create_withInvalidData_shouldReturn400() {
        List<CreateEventDTO> invalidData = List.of(
                new CreateEventDTO(null, null, null, null),
                new CreateEventDTO("", "", null, null),
                new CreateEventDTO("Event 1", "12345678", null, null),
                new CreateEventDTO("Event 1", "12345678", Instant.now(), null),
                new CreateEventDTO("Event 1", "12345678", Instant.now(), Instant.now().minus(1, ChronoUnit.DAYS)),
                new CreateEventDTO("Event 1", "12345678", Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS)),
                new CreateEventDTO(
                        "Event 1",
                        "89025-302",
                        Instant.now().plus(2, ChronoUnit.DAYS),
                        Instant.now().plus(1, ChronoUnit.DAYS)));
        invalidData.forEach(dto -> {
            String baseUrl = "http://localhost:" + port + "/api/v1/events";
            ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(baseUrl + "/create", dto, ErrorMessage.class);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("/api/v1/events/create", response.getBody().getPath());
            assertEquals(response.getBody().getStatus(), HttpStatus.BAD_REQUEST.value());
            assertEquals("POST", response.getBody().getMethod());
        });
    }

    @Test
    public void findById_withValidId_shouldReturn200andBody() {
        Event event;
        event = new Event(
                "Event 1",
                "89025-302",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS),
                "Address 1",
                "City 1",
                "District 1",
                StatesEnum.AC);

        UUID id = eventRepository.save(event).getId();

        String baseUrl = "http://localhost:" + port + "/api/v1/events";
        ResponseEntity<EventResponseDTO> response = restTemplate.getForEntity(baseUrl + "/" + id, EventResponseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(event.getName(), response.getBody().getName());
        assertEquals(event.getCep(), response.getBody().getCep());
        assertEquals(event.getAddress(), response.getBody().getAddress());
        assertEquals(event.getCity(), response.getBody().getCity());
        assertEquals(event.getDistrict(), response.getBody().getDistrict());
        assertEquals(event.getState(), response.getBody().getState());
    }

    @Test
    public void findById_withInvalidId_shouldReturn404() {
        UUID id = UUID.randomUUID();
        ResponseEntity<ErrorMessage> response =
                restTemplate.getForEntity("http://localhost:" + port + "/api/v1/events/" + id, ErrorMessage.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getPath(), "/api/v1/events/" + id);
        assertEquals(response.getBody().getStatus(), HttpStatus.NOT_FOUND.value());
        assertEquals("GET", response.getBody().getMethod());
    }

    @Test
    public void findAll_shouldReturn200andBody() {
        List<Event> events = getEvents();
        eventRepository.saveAll(events);

        int page = 0;
        int size = 10;
        String baseUrl = "http://localhost:" + port + "/api/v1/events";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                baseUrl + "?page=" + page + "&size=" + size,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("content"));
        assertTrue(body.containsKey("totalElements"));
        assertTrue(body.containsKey("totalPages"));
    }

    @Test
    public void findAll_withNoData_shouldReturn200andEmptyList() {
        String baseUrl = "http://localhost:" + port + "/api/v1/events";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                baseUrl + "?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );


        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("content"));
        assertTrue(body.containsKey("totalElements"));
    }

    @Test
    public void update_withValidData_shouldReturn200andBody() {
        Event event = getEvents().get(0);
        eventRepository.save(event);

        UpdateEventRequestDTO updateEventInternalDTO = new UpdateEventRequestDTO(
               "Updated Event 1",
                event.getCep(),
                "Updated Address 1",
                event.getCity(),
                event.getDistrict(),
                event.getState()
        );

        String baseUrl = "http://localhost:" + port + "/api/v1/events/" + event.getId();
        ResponseEntity<EventResponseDTO> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(updateEventInternalDTO),
                EventResponseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(event.getId(), response.getBody().getId());
        assertEquals(updateEventInternalDTO.getName(), response.getBody().getName());
        assertEquals(event.getCep(), response.getBody().getCep());
        assertEquals(updateEventInternalDTO.getAddress(), response.getBody().getAddress());
        assertEquals(event.getCity(), response.getBody().getCity());
        assertEquals(event.getDistrict(), response.getBody().getDistrict());
        assertEquals(event.getState(), response.getBody().getState());
    }

    @Test
    public void update_withInvalidId_shouldReturn404() {
        UUID id = UUID.randomUUID();
        UpdateEventRequestDTO updateEventInternalDTO = new UpdateEventRequestDTO(
                "Updated Event 1",
                "89025-302",
                "Updated Address 1",
                "Updated City 1",
                "Updated District 1",
                StatesEnum.AC);

        String baseUrl = "http://localhost:" + port + "/api/v1/events/" + id;
        ResponseEntity<ErrorMessage> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(updateEventInternalDTO),
                ErrorMessage.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getPath(), "/api/v1/events/" + id);
        assertEquals(response.getBody().getStatus(), HttpStatus.NOT_FOUND.value());
        assertEquals("PUT", response.getBody().getMethod());
    }

    @Test
    public void update_withInvalidData_shouldReturn400() {
        Event event = getEvents().get(0);
        eventRepository.save(event);

        UpdateEventRequestDTO updateEventInternalDTO = new UpdateEventRequestDTO(
                null,
                event.getCep(),
                event.getAddress(),
                event.getCity(),
                event.getDistrict(),
                event.getState()
        );

        String baseUrl = "http://localhost:" + port + "/api/v1/events/" + event.getId();
        ResponseEntity<ErrorMessage> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                new HttpEntity<>(updateEventInternalDTO),
                ErrorMessage.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getPath(), "/api/v1/events/" + event.getId());
        assertEquals(response.getBody().getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals("PUT", response.getBody().getMethod());
    }

    private List<Event> getEvents() {
        Event event1 = new Event(
                "Test Event 1",
                "75000-000",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS),
                "Test Address 1",
                "Test City 1",
                "Test District 1",
                StatesEnum.AC);

        Event event2 = new Event(
                "Test Event 2",
                "75000-000",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS),
                "Test Address 2",
                "Test City 2",
                "Test District 2",
                StatesEnum.AC);

        Event event3 = new Event(
                "Test Event 3",
                "75000-000",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(2, ChronoUnit.DAYS),
                "Test Address 3",
                "Test City 3",
                "Test District 3",
                StatesEnum.AC);

        return Arrays.asList(event1, event2, event3);
    }
}
