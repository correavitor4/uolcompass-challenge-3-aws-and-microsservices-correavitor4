package compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto;

import compassoulspring2024pb.challenge1.eventservice.model.enums.StatesEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateEventRequestDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "cep must be in the format 00000-000")
    private String cep;

    @NotBlank(message = "address cannot be blank")
    private String address;

    @NotBlank(message = "city cannot be blank")
    private String city;

    @NotBlank(message = "district cannot be blank")
    private String district;

    @NotNull
    private StatesEnum state;
}
