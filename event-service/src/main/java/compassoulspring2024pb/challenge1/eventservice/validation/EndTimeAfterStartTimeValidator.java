package compassoulspring2024pb.challenge1.eventservice.validation;

import compassoulspring2024pb.challenge1.eventservice.web.api.v1.dto.CreateEventDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndTimeAfterStartTimeValidator implements ConstraintValidator<EndTimeAfterStartTime, CreateEventDTO> {
    @Override
    public boolean isValid(CreateEventDTO dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        if (dto.getStartTime() == null || dto.getEndTime() == null) return false;

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("End time must be after start time")
                    .addPropertyNode("endTime")  // Associa o erro ao campo endTime
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
