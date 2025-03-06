package compassoulspring2024pb.challenge1.eventservice.repository;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface EventRepository extends MongoRepository<Event, UUID> {
}
