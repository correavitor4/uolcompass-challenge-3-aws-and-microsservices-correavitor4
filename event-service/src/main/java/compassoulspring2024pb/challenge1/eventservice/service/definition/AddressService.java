package compassoulspring2024pb.challenge1.eventservice.service.definition;

import compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto.ViaCepResponseDTO;

public interface AddressService {
    ViaCepResponseDTO findByCep(String cep);
}
