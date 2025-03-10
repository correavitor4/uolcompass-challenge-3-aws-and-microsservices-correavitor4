package compassoulspring2024pb.challenge1.eventservice.validation;

import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, CreateEventDTO>  {
    @Override
    public boolean isValid(CreateEventDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto == null) return true;

        if (dto.getStartTime() == null || dto.getEndTime() == null) return false;

        return dto.getStartTime().isBefore(dto.getEndTime());
    }
}
