package compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import compassoulspring2024pb.challenge1.msticketmanager.model.enums.TicketStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class TicketResponseDTO {
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

}
