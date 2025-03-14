package compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto;

import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViaCepResponseDTO {
    private String logradouro;
    private String bairro;
    private String localidade;

    @NotNull
    private StatesEnum uf;

    @NotBlank
    private String cep;

}
