package compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto;

import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import lombok.Data;

@Data
public class ViaCepResponseDTO {
    private String logradouro;
    private String bairro;
    private String localidade;
    private StatesEnum uf;
    private String cep;
}
