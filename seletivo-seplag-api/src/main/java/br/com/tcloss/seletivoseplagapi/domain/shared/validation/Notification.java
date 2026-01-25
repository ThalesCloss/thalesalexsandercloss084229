package br.com.tcloss.seletivoseplagapi.domain.shared.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

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

    public <T> T tryExecute(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            this.errors.add(ex.getMessage());
            return null;
        }
    }

    public void tryExecute(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            this.errors.add(ex.getMessage());
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
