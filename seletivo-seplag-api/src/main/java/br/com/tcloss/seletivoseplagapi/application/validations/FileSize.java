package br.com.tcloss.seletivoseplagapi.application.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidator.class)
@Documented
public @interface FileSize {
    String message() default "The file exceeds the maximum allowed size";    
    long maxSizeBytes();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
