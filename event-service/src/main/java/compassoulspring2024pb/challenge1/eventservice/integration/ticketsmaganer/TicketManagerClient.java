package compassoulspring2024pb.challenge1.eventservice.integration.ticketsmaganer;

import compassoulspring2024pb.challenge1.eventservice.integration.ticketsmaganer.dto.GetTicketClientResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-ticket-manager", url = "${ticket-manager.url}/api/v1/tickets")
public interface TicketManagerClient {
    @GetMapping("getByEventId/{eventId}")
    ResponseEntity<GetTicketClientResponseDTO> getByEventId(@PathVariable UUID eventId);
}
