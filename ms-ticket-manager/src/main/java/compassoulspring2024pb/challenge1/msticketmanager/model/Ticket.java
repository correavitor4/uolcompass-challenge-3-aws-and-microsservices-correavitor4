package compassoulspring2024pb.challenge1.msticketmanager.model;

import compassoulspring2024pb.challenge1.msticketmanager.model.enums.TicketStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Document(collection = "tickets")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Ticket {
    @Id
    private UUID id = UUID.randomUUID();

    private Instant createdAt = Instant.now();

    @NotBlank
    private String cpf;

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerEmail;

    @NotNull
    private UUID eventId;

    @PositiveOrZero
    private BigDecimal totalAmountBRL;

    private BigDecimal totalAmountUSD;

    private TicketStatusEnum status = TicketStatusEnum.PAID;
    private Instant cancelledAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;
        return id.equals(ticket.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
