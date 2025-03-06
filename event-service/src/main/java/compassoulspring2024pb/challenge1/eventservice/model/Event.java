package compassoulspring2024pb.challenge1.eventservice.model;

import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "events")
@Getter
public class Event {
    @Id
    private UUID id = UUID.randomUUID();

    private String name;
    private Instant createdAt;
    private String cep;
    private String address;
    private String city;
    private String district;

    private StatesEnum state;

    public Event(String name,
                 Instant createdAt,
                 String cep,
                 String address,
                 String city,
                 String district,
                 StatesEnum state) {
        this.name = name;
        this.createdAt = createdAt;
        this.cep = cep;
        this.address = address;
        this.city = city;
        this.district = district;
        this.state = state;
    }
}
