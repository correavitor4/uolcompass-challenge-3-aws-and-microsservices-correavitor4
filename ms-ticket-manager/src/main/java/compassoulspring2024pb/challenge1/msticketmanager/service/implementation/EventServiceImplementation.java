package compassoulspring2024pb.challenge1.msticketmanager.service.implementation;

import compassoulspring2024pb.challenge1.msticketmanager.exception.api.ApiInternalServerException;
import compassoulspring2024pb.challenge1.msticketmanager.integration.event_manager.EventManagerClient;
import compassoulspring2024pb.challenge1.msticketmanager.integration.event_manager.dto.EventResponseClientDTO;
import compassoulspring2024pb.challenge1.msticketmanager.service.definition.EventService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImplementation implements EventService {

    private final EventManagerClient eventManagerClient;

    @Override
    public boolean existsById(UUID id) throws ApiInternalServerException {
        try {
            ResponseEntity<EventResponseClientDTO> response = eventManagerClient.findById(id);

            return response.getBody() != null && Objects.equals(response.getBody().getId(), id);
        } catch (FeignException.NotFound e) {
            return false;
        } catch (FeignException e) {
            log.error("Error during get event from event manager: {}", e.getMessage());
            throw new ApiInternalServerException(e.getMessage());
        }
    }
}
