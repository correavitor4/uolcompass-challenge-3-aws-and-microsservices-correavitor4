package compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EventResponseDTO {
    private UUID id;
    private String name;
    private Instant createdAt;
    private String cep;
    private String address;
    private String city;
    private String district;

    public static EventResponseDTO fromModel(Event event) {
        EventResponseDTO eventResponseDTO = new EventResponseDTO();
        eventResponseDTO.setId(event.getId());
        eventResponseDTO.setName(event.getName());
        eventResponseDTO.setCreatedAt(event.getCreatedAt());
        eventResponseDTO.setCep(event.getCep());
        eventResponseDTO.setAddress(event.getAddress());
        eventResponseDTO.setCity(event.getCity());
        eventResponseDTO.setDistrict(event.getDistrict());
        return eventResponseDTO;
    }
}
