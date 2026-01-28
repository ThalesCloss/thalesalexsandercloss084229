package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.util.regex.Pattern;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;
import jakarta.persistence.Embeddable;

@Embeddable
public record ISRC(String isrc) implements ValueObject {
    private static final Pattern ISRC_PATTERN = Pattern.compile("^\\w{2}-\\w{3}-\\d{2}-\\d{5}$");

    public ISRC(String isrc) {
        if (isrc == null || !ISRC_PATTERN.matcher(isrc).matches()) {
            throw new IllegalArgumentException("ISRC inválido: Deve seguir o formato XX-XXX-YY-NNNNN");
        }
        this.isrc = isrc;
    }
}
