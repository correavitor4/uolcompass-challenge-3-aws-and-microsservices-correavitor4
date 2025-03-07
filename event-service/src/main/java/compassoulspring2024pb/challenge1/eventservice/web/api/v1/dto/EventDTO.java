package compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class EventDTO {
    private UUID id;
    private String name;
    private Instant createdAt;
    private String cep;
    private String address;
    private String city;
    private String district;

    public static EventDTO fromModel(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setName(event.getName());
        eventDTO.setCreatedAt(event.getCreatedAt());
        eventDTO.setCep(event.getCep());
        eventDTO.setAddress(event.getAddress());
        eventDTO.setCity(event.getCity());
        eventDTO.setDistrict(event.getDistrict());
        return eventDTO;
    }
}
