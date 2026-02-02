package br.com.tcloss.seletivoseplagapi.application.validations;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, FileUpload> {

    private long maxSizeBytes;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        maxSizeBytes = constraintAnnotation.maxSizeBytes();
    }

    @Override
    public boolean isValid(FileUpload value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.size() <= maxSizeBytes;
    }

}