package compassoulspring2024pb.challenge1.eventservice.model;

import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "events")
@Data
public class Event {
    @Id
    private UUID id = UUID.randomUUID();

    private String name;
    private Instant createdAt = Instant.now();
    private String cep;
    private String address;
    private String city;
    private String district;

    private Instant deletedAt;

    private StatesEnum state;

    public Event(String name,
                 String cep,
                 String address,
                 String city,
                 String district,
                 StatesEnum state) {
        this.name = name;
        this.cep = cep;
        this.address = address;
        this.city = city;
        this.district = district;
        this.state = state;
    }

    public void setAsDeleted(){
        this.deletedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
