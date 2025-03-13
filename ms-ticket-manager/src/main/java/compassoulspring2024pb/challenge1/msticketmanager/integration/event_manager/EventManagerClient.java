package compassoulspring2024pb.challenge1.msticketmanager.integration.event_manager;

import compassoulspring2024pb.challenge1.msticketmanager.integration.event_manager.dto.EventResponseClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "event-manager", url = "${event-manager.url}/api/v1/events")
public interface EventManagerClient {
    @GetMapping("/{id}")
    ResponseEntity<EventResponseClientDTO> findById(@PathVariable UUID id);
}
