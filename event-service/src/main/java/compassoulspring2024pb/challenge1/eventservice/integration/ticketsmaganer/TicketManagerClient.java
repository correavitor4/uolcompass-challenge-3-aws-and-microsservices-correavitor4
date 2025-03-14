package compassoulspring2024pb.challenge1.eventservice.integration.ticketsmaganer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "ms-ticket-manager", url = "${ticket-manager.url}/api/v1/tickets")
public interface TicketManagerClient {
    @GetMapping("/existsByEventId/{eventId}")
    boolean getByEventId(@PathVariable UUID eventId);
}
