package br.com.tcloss.seletivoseplagapi.domain.shared.validation;

import java.util.List;

public class DomainException extends RuntimeException {
   private final List<String> errors;

    public DomainException(String message) {
        super(message);
        this.errors = List.of(message);
    }

    public DomainException(List<String> errors) {
        super(String.join(", ", errors)); 
        this.errors = List.copyOf(errors); 
    }

    public List<String> getErrors() {
        return errors;
    }

}
