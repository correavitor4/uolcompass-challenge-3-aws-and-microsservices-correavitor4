package compassoulspring2024pb.challenge1.eventservice.service.implementation;

import compassoulspring2024pb.challenge1.eventservice.exception.APIInternalServerErrorException;
import compassoulspring2024pb.challenge1.eventservice.exception.via_cep_api.ViaCepAPICepNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.exception.via_cep_api.ViaCepApiUnavailableException;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.ViaCepClient;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto.ViaCepResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.service.definition.AddressService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImplementation implements AddressService {

    private final ViaCepClient viaCepClient;

    @Override
    public ViaCepResponseDTO findByCep(String cep) {
        try {
            return viaCepClient.findByCep(cep).getBody();
        } catch (FeignException e) {
            if (e instanceof FeignException.NotFound
                    || e instanceof FeignException.BadRequest) {
                log.warn(e.getMessage());
                throw new ViaCepAPICepNotFoundException(e.getMessage());
            }

            if (e instanceof FeignException.InternalServerError
                    || e instanceof FeignException.ServiceUnavailable) {
                log.warn(e.getMessage());
                throw new ViaCepApiUnavailableException(e.getMessage());
            }

            log.error(e.getMessage());
            throw new APIInternalServerErrorException(e.getMessage());
        }
    }
}