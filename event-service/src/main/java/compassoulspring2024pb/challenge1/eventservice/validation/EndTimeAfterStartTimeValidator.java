package compassoulspring2024pb.challenge1.eventservice.validation;

import compassoulspring2024pb.challenge1.eventservice.service.dto.CreateEventInternalDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, CreateEventInternalDTO>  {
    @Override
    public boolean isValid(CreateEventInternalDTO dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto == null) return true;

        if (dto.getStartTime() == null || dto.getEndTime() == null) return false;

        return dto.getStartTime().isBefore(dto.getEndTime());
    }
}
