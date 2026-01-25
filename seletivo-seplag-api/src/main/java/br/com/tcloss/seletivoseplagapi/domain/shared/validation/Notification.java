package br.com.tcloss.seletivoseplagapi.domain.shared.validation;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    private final List<String> errors = new ArrayList<>();

    private Notification() {}

    public static Notification create() {
        return new Notification();
    }

    public void append(String error) {
        this.errors.add(error);
    }
    
    public void validate(boolean condition, String errorMessage) {
        if (!condition) {
            this.errors.add(errorMessage);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public void throwIfHasErrors() {
        if (hasErrors()) {
            throw new DomainException(this.errors);
        }
    }
    
    public String errorMessage() {
        return String.join(", ", errors);
    }
}
