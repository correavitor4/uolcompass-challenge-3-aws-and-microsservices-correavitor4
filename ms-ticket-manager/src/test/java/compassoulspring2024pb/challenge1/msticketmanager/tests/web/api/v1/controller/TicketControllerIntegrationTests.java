package compassoulspring2024pb.challenge1.msticketmanager.tests.web.api.v1.controller;

import compassoulspring2024pb.challenge1.msticketmanager.exception.api.message.ErrorMessage;
import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import compassoulspring2024pb.challenge1.msticketmanager.repository.TicketRepository;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.EventService;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.CreateTicketRequestDTO;
import compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto.TicketResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketControllerIntegrationTests {
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private EventService eventService;

    @LocalServerPort
    private int port;

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    public void tearDown() {
        ticketRepository.deleteAll();
    }


    @BeforeEach
    public void setup() {
        ticketRepository.deleteAll();
    }


    @Test
    public void create_withValidInputData_shouldCreateTicketAndReturnIt() {
        CreateTicketRequestDTO createTicketRequestDTO = new CreateTicketRequestDTO(
                "293.666.570-10",
                "John Doe",
                "4k4lG@example.com",
                UUID.randomUUID(),
                BigDecimal.valueOf(100)
        );

        when(eventService.existsById(any(UUID.class))).thenReturn(true);

        String url = "http://localhost:" + port + "/api/v1/tickets/create";

        ResponseEntity<TicketResponseDTO> response = restTemplate.postForEntity(url, createTicketRequestDTO, TicketResponseDTO.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(
                Objects.requireNonNull(
                                response
                                        .getHeaders()
                                        .getLocation())
                        .toString()
                        .contains("http://localhost:" + port + "/api/v1/tickets/"));
        Assertions.assertEquals(createTicketRequestDTO.getEventId(), response.getBody().getEventId());
        Assertions.assertEquals(createTicketRequestDTO.getCustomerName(), response.getBody().getCustomerName());
        Assertions.assertEquals(createTicketRequestDTO.getCustomerEmail(), response.getBody().getCustomerEmail());
        Assertions.assertEquals(createTicketRequestDTO.getEventId(), response.getBody().getEventId());
        Assertions.assertEquals(createTicketRequestDTO.getTotalAmountBRL(), response.getBody().getTotalAmountBRL());
    }

    @Test
    public void create_withInvalidInputEventId_shouldReturnBadRequest() {
        CreateTicketRequestDTO createTicketRequestDTO = new CreateTicketRequestDTO(
                "293.666.570-10",
                "John Doe",
                "4k4lG@example.com",
                UUID.randomUUID(),
                BigDecimal.valueOf(100)
        );

        when(eventService.existsById(any(UUID.class))).thenReturn(false);

        String url = "http://localhost:" + port + "/api/v1/tickets/create";

        ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url, createTicketRequestDTO, ErrorMessage.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNull(response.getHeaders().getLocation());
        Assertions.assertEquals("/api/v1/tickets/create", response.getBody().getPath());
        Assertions.assertEquals(response.getBody().getStatus(), HttpStatus.BAD_REQUEST.value());
        Assertions.assertEquals("POST", response.getBody().getMethod());
    }

    @Test
    public void create_withInvalidInputData_shouldReturnBadRequest() {
        List<CreateTicketRequestDTO> invalidData = getCreateTicketRequestDTOs();
        invalidData.forEach(dto -> {
            String url = "http://localhost:" + port + "/api/v1/tickets/create";
            ResponseEntity<ErrorMessage> response = restTemplate.postForEntity(url, dto, ErrorMessage.class);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertNull(response.getHeaders().getLocation());
            Assertions.assertEquals("/api/v1/tickets/create", response.getBody().getPath());
            Assertions.assertEquals(response.getBody().getStatus(), HttpStatus.BAD_REQUEST.value());
            Assertions.assertEquals("POST", response.getBody().getMethod());
        });
    }

    @Test
    public void getById_withValidId_shouldReturnTicket() {
        CreateTicketRequestDTO createTicketRequestDTO = new CreateTicketRequestDTO(
                "293.666.570-10",
                "John Doe",
                "4k4lG@example.com",
                UUID.randomUUID(),
                BigDecimal.valueOf(100)
        );


        UUID ticketId = ticketRepository.save(modelMapper.map(createTicketRequestDTO, Ticket.class)).getId();
        String url = "http://localhost:" + port + "/api/v1/tickets/" + ticketId;
        ResponseEntity<TicketResponseDTO> response = restTemplate.getForEntity(url, TicketResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(createTicketRequestDTO.getEventId(), response.getBody().getEventId());
        Assertions.assertEquals(createTicketRequestDTO.getCustomerName(), response.getBody().getCustomerName());
        Assertions.assertEquals(createTicketRequestDTO.getCustomerEmail(), response.getBody().getCustomerEmail());
        Assertions.assertEquals(createTicketRequestDTO.getEventId(), response.getBody().getEventId());
        Assertions.assertEquals(createTicketRequestDTO.getTotalAmountBRL(), response.getBody().getTotalAmountBRL());
    }

    @Test
    public void getById_withNonExistentId_shouldReturnNotFound() {
        UUID ticketId = UUID.randomUUID();
        String url = "http://localhost:" + port + "/api/v1/tickets/" + ticketId;
        ResponseEntity<ErrorMessage> response = restTemplate.getForEntity(url, ErrorMessage.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNull(response.getHeaders().getLocation());
        Assertions.assertEquals(response.getBody().getPath(), "/api/v1/tickets/" + ticketId);
        Assertions.assertEquals(response.getBody().getStatus(), HttpStatus.NOT_FOUND.value());
        Assertions.assertEquals("GET", response.getBody().getMethod());
    }

    @Test
    public void getByCPF_withValidCPF_shouldReturnTicket() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .cpf("293.666.570-10")
                .customerName("John Doe")
                .customerEmail("4k4lG@example.com")
                .eventId(eventId)
                .totalAmountBRL(BigDecimal.valueOf(100))
                .build();

        ticketRepository.save(ticket);

        // Act
        String url = "http://localhost:" + port + "/api/v1/tickets/findByCpf/" + ticket.getCpf();
        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().get("totalElements"));
        Assertions.assertEquals(1, response.getBody().get("totalPages"));
    }

    @Test
    public void getByCPF_withNonExistentCPF_shouldReturnAnEmptyList() {
        String url = "http://localhost:" + port + "/api/v1/tickets/findByCpf/293.666.570-10";

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().get("totalElements"));
        Assertions.assertEquals(0, response.getBody().get("totalPages"));
    }

    @Test
    public void findAll_withNoTickets_shouldReturnEmptyList() {
        String url = "http://localhost:" + port + "/api/v1/tickets";
        ResponseEntity<Map<String, Object>> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(0, response.getBody().get("totalElements"));
        Assertions.assertEquals(0, response.getBody().get("totalPages"));
    }

    @Test
    public void findAll_withPageSize20_shouldReturn20Tickets() {
        List<Ticket> ticketList = getTicketList();

        ticketRepository.saveAll(ticketList);

        String url = "http://localhost:" + port + "/api/v1/tickets";
        ResponseEntity<Map<String, Object>> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(30, response.getBody().get("totalElements"));
        Assertions.assertEquals(2, response.getBody().get("totalPages"));
        Assertions.assertEquals(20, response.getBody().get("size"));
    }

    @Test
    public void findAll_withPage0_shouldReturnFirstPage() {
        List<Ticket> ticketList = getTicketList();

        ticketRepository.saveAll(ticketList);

        String url = "http://localhost:" + port + "/api/v1/tickets";
        ResponseEntity<Map<String, Object>> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(30, response.getBody().get("totalElements"));
        Assertions.assertEquals(2, response.getBody().get("totalPages"));
        Assertions.assertEquals(20, response.getBody().get("size"));
    }

    @Test
    public void findAll_withPage1_shouldReturnSecondPage() {
        List<Ticket> ticketList = getTicketList();

        ticketRepository.saveAll(ticketList);

        String url = "http://localhost:" + port + "/api/v1/tickets?page=1";
        ResponseEntity<Map<String, Object>> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(30, response.getBody().get("totalElements"));
        Assertions.assertEquals(2, response.getBody().get("totalPages"));
        Assertions.assertEquals(20, response.getBody().get("size"));
    }

    @Test
    public void existsByEventId_withValidEventId_shouldReturnTrue() {
        UUID eventId = UUID.randomUUID();

        List<Ticket> ticketList = getTicketListByEventId(eventId);

        ticketRepository.saveAll(ticketList);

        String url = "http://localhost:" + port + "/api/v1/tickets/existsByEventId/" + eventId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody());
    }

    @Test
    public void existsByEventId_withNonExistentEventId_shouldReturnFalse() {
        UUID eventId = UUID.randomUUID();

        String url = "http://localhost:" + port + "/api/v1/tickets/existsByEventId/" + eventId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody());
    }

    private List<CreateTicketRequestDTO> getCreateTicketRequestDTOs() {
        return List.of(
                new CreateTicketRequestDTO(null, null, null, null, null),
                new CreateTicketRequestDTO("293.666.570-10", null, null, null, null),
                new CreateTicketRequestDTO("293.666.570-10", "John Doe", null, null, null),
                new CreateTicketRequestDTO("293.666.570-10", "John Doe", "4k4lG@example.com", null, null),
                new CreateTicketRequestDTO("293.666.570-10", "John Doe", "4k4lG@example.com", UUID.randomUUID(), null),
                new CreateTicketRequestDTO("293.666.570-10", "John Doe", "4k4lG@example.com", UUID.randomUUID(), BigDecimal.valueOf(-1))
        );
    }

    private List<Ticket> getTicketList() {
        return Stream.iterate(0, i -> i + 1)
                .limit(30)
                .map(i -> Ticket.builder()
                        .id(UUID.randomUUID())
                        .cpf("293.666.570-10")
                        .customerName("John Doe")
                        .customerEmail("4k4lG@example.com")
                        .eventId(UUID.randomUUID())
                        .totalAmountBRL(BigDecimal.valueOf(100))
                        .build())
                .collect(Collectors.toList());
    }

    private List<Ticket> getTicketListByEventId(UUID eventId) {
        return Stream.iterate(0, i -> i + 1)
                .limit(10)
                .map(i -> Ticket.builder()
                        .id(UUID.randomUUID())
                        .cpf("293.666.570-10")
                        .customerName("John Doe")
                        .customerEmail("4k4lG@example.com")
                        .eventId(eventId)
                        .totalAmountBRL(BigDecimal.valueOf(100))
                        .build())
                .collect(Collectors.toList());
    }
}
