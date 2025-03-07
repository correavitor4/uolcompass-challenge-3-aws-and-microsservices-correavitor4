package compassoulspring2024pb.challenge1.eventservice.service.dto;

import compassoulspring2024pb.challenge1.eventservice.integration.viacep.dto.ViaCepResponseDTO;
import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import compassoulspring2024pb.challenge1.eventservice.validation.EndTimeAfterStartTime;
import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class CreateEventInternalDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Pattern(regexp = "^\\d{5}-\\d{3}$\n", message = "cep must be in the format 00000-000")
    private String cep;

    @NotNull
    @Future
    private Instant startTime;

    @NotNull
    @Future
    @EndTimeAfterStartTime
    private Instant endTime;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotBlank(message = "city cannot be blank")
    private String city;

    @NotBlank(message = "district cannot be blank")
    private String district;

    @NotBlank(message = "state cannot be blank")
    private StatesEnum state;

    public Event toModel() {
        return new Event(name, cep, address, city, district, state);
    }

    public CreateEventInternalDTO(@Valid CreateEventDTO dto, @Valid ViaCepResponseDTO address){
        this.name = dto.getName();
        this.cep = dto.getCep();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.address = address.getLogradouro();
        this.city = address.getLocalidade();
        this.district = address.getBairro();
        this.state = address.getUf();
    }
}
