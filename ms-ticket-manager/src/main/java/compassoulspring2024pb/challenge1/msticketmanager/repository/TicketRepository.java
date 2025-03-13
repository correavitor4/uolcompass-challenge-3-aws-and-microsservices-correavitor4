package compassoulspring2024pb.challenge1.msticketmanager.repository;

import compassoulspring2024pb.challenge1.msticketmanager.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends MongoRepository<Ticket, UUID>
{
    @Query("{'cancelledAt': null, 'id': ?0}")
    Optional<Ticket> findActiveById(UUID id);

    @Query("{'cancelledAt': null ,'cpf': ?0}")
    Optional<Ticket> findActiveByCpf(String cpf);

    @Query("{'cancelledAt': null}")
    Page<Ticket> findAllActive(Pageable pageable);
}
