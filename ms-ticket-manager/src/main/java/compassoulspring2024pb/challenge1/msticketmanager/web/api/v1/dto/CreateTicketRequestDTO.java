package compassoulspring2024pb.challenge1.msticketmanager.web.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateTicketRequestDTO {

    @NotBlank

    @CPF
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

}
