package compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto;

import compassoulspring2024pb.challenge1.eventservice.model.Event;
import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateEventDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Pattern(regexp = "^\\d{5}-\\d{3}$\n", message = "cep must be in the format 00000-000")
    private String cep;

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
}
