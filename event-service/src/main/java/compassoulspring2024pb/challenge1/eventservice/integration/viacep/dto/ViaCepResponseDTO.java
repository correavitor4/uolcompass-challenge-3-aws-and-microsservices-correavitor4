package compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto;

import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ViaCepResponseDTO {

    private String logradouro;
    private String bairro;
    private String localidade;

    @NotBlank
    private StatesEnum uf;

    @NotBlank
    private String cep;
}
