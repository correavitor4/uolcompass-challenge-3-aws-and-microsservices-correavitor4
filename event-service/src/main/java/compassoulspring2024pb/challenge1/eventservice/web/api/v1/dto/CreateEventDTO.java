package compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto;

import compassoulspring2024pb.challenge1.eventservice.validation.EndTimeAfterStartTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@EndTimeAfterStartTime
public class CreateEventDTO {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "cep must be in the format 00000-000")
    private String cep;

    @NotNull
    @Future
    private Instant startTime;

    @NotNull
    @Future
    private Instant endTime;
}
