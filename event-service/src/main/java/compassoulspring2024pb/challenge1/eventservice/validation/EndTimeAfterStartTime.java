package compassoulspring2024pb.challenge1.eventservice.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EndTimeAfterStartTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndTimeAfterStartTime {
    String message() default "End time must be after start time";
    Class<?>[] groups() default {};
    Class<? extends java.lang.annotation.ElementType>[] payload() default {};
}
