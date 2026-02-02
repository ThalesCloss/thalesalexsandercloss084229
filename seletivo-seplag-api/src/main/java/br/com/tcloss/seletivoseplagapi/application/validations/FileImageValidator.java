package br.com.tcloss.seletivoseplagapi.application.validations;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FileImageValidator implements ConstraintValidator<FileImage, FileUpload>{

    @Override
    public boolean isValid(FileUpload value, ConstraintValidatorContext context) {
        if(value ==null) {
            return false;
        }
        final String contentType = value.contentType();
        return contentType.contains("image/");            
    }

}
