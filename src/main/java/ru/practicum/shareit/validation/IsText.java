package ru.practicum.shareit.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextValidator.class)
public @interface IsText {

    String message() default "data is not valid";

    Class<?>[] groups() default  { };

    Class<? extends Payload>[] payload() default  { };



}
