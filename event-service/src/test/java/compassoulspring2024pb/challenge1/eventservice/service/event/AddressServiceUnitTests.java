package compassoulspring2024pb.challenge1.eventservice.service.event;

import compassoulspring2024pb.challenge1.eventservice.exception.api.APIInternalServerErrorException;
import compassoulspring2024pb.challenge1.eventservice.exception.via_cep_api.ViaCepAPICepNotFoundException;
import compassoulspring2024pb.challenge1.eventservice.exception.via_cep_api.ViaCepApiUnavailableException;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.ViaCepClient;
import compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto.ViaCepResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.service.definition.AddressService;
import compassoulspring2024pb.challenge1.eventservice.service.implementation.AddressServiceImplementation;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest()
public class AddressServiceUnitTests {
    @Autowired
    private AddressService addressService;

    @Mock
    private ViaCepClient viaCepClient;

    private AddressService mockAddressService() {
        return new AddressServiceImplementation(viaCepClient);
    }

    @Test
    public void findByCep_withValidCep_shouldReturnAddress() {
        String cep = "65049-232";
        ViaCepResponseDTO address = addressService.findByCep(cep);

        assertNotNull(address);
        assertEquals(cep, address.getCep());
        assertFalse(address.getUf().toString().isBlank());
        assertFalse(address.getCep().isBlank());
    }

    @Test
    public void findByCep_withInvalidCep_shouldThrowException() {
        List<String> invalidCeps = List.of(
                "666555",
                "444444445464654646",
                "");

        invalidCeps.forEach(cep -> assertThrows(ViaCepAPICepNotFoundException.class, () -> addressService.findByCep(cep)));
    }

    @Test
    public void findByCep_validCpfAndUnavailableViaCepApiService_shouldThrowException() {
        when(viaCepClient.findByCep(any(String.class))).thenThrow(FeignException.ServiceUnavailable.class);
        assertThrows(ViaCepApiUnavailableException.class, () -> mockAddressService().findByCep("65049-232"));
    }

    @Test
    public void findByCep_validCpfAndViaCepApiServiceReturnsInternalServerError_shouldThrowException() {
        when(viaCepClient.findByCep(any(String.class))).thenThrow(FeignException.InternalServerError.class);
        assertThrows(ViaCepApiUnavailableException.class, () -> mockAddressService().findByCep("65049-232"));
    }

    @Test
    public void findByCep_withValidCPFAndAndErrorThatsIsNotFeignException_shouldThrowException() {
        when(viaCepClient.findByCep(any(String.class))).thenThrow(RuntimeException.class);
        assertThrows(APIInternalServerErrorException.class, () -> mockAddressService().findByCep("65049-232"));
    }
}