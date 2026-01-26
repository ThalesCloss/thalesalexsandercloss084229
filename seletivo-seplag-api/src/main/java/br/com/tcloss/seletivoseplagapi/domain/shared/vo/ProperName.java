package br.com.tcloss.seletivoseplagapi.domain.shared.vo;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public record ProperName(
    String name
) implements ValueObject{
    public ProperName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        
        if (name.length() < 2) {
             throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        name = name.trim();
    }

    @Override
    public final String toString() {
        return name;
    }
}
