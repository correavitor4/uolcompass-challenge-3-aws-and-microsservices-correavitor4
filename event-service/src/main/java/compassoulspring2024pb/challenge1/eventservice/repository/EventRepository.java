package compassoulspring2024pb.challenge1.eventservice.repository;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends MongoRepository<Event, UUID> {
    @Query("{'deletedAt': null}")
    Optional<Event> findActiveById(UUID id);

    @Query("{'deletedAt': null}")
    Page<Event> findAllActive(Pageable pageable);
}
