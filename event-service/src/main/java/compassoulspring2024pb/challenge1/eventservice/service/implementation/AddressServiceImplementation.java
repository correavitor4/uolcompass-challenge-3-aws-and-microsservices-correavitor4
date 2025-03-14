package compassoulspring2024pb.challenge1.eventservice.service.implementation;

import compassoulspring2024pb.challenge1.eventservice.exception.api.APIInternalServerErrorException;
import compassoulspring2024pb.challenge1.eventservice.exception.via_cep_api.ViaCepAPICepNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.exception.via_cep_api.ViaCepApiUnavailableException;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.ViaCepClient;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto.ViaCepResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.service.definition.AddressService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImplementation implements AddressService {

    private final ViaCepClient viaCepClient;

    @Override
    public ViaCepResponseDTO findByCep(String cep)
            throws ViaCepAPICepNotFoundException,
            ViaCepApiUnavailableException,
            APIInternalServerErrorException {

        try {

            ResponseEntity<ViaCepResponseDTO> response = viaCepClient.findByCep(cep);

            ViaCepResponseDTO address = response.getBody();

            if (!getAddressErrors(address).isEmpty()) {
                StringJoiner joiner = new StringJoiner("\n");
                for (String s : getAddressErrors(address)) {
                    joiner.add(s);
                }
                throw new ViaCepAPICepNotFoundException(
                        joiner.toString());
            }

            return response.getBody();
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
        } catch (ViaCepAPICepNotFoundException e) {
            log.error(e.getMessage());
            throw new ViaCepAPICepNotFoundException(e.getMessage());
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new APIInternalServerErrorException(e.getMessage());
        }
    }

    private List<String> getAddressErrors(ViaCepResponseDTO address) {
        List<String> errors = new ArrayList<>();

        if (address == null) {
            errors.add("Address not found");
        }

        if (address.getUf() == null) {
            errors.add("The provided cep results in an invalid UF");
        }

        if (address.getLogradouro() == null) {
            errors.add("The provided cep results in an invalid logradouro");
        }

        if (address.getLocalidade() == null) {
            errors.add("The provided cep results in an invalid localidade");
        }

        if (address.getBairro() == null) {
            errors.add("The provided cep results in an invalid bairro");
        }

        if (address.getCep() == null) {
            errors.add("The provided cep results in an invalid cep");
        }

        return errors;
    }
}