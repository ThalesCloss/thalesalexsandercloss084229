package br.com.tcloss.seletivoseplagapi.domain.model.composition;

import java.util.regex.Pattern;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public record ISWC(String value) implements ValueObject {
    private static final Pattern ISWC_REGEX = Pattern.compile("^T-\\d{3}\\.\\d{3}\\.\\d{3}-\\w$");
    public ISWC {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ISWC não pode ser vazio");
        }

        String trimmedValue = value.trim();

        if (!ISWC_REGEX.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("ISWC inválido. Formato esperado: T-123.456.789-0");
        }

        value = trimmedValue;
    }

    @Override
    public final String toString() {
        return value;
    }
}
