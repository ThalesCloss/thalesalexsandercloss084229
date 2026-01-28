package br.com.tcloss.seletivoseplagapi.domain.model.album;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record Image(
        @Column(name = "identifier", nullable = false) String identifier,
        @Column(name = "extension", nullable = false, length = 5) String extension,
        @Enumerated(EnumType.STRING) @Column(name = "type", nullable = false, length = 20) ImageType type)
        implements ValueObject {

    public Image {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("O identificador da imagem é obrigatório.");
        }
        if (extension == null || extension.isBlank()) {
            throw new IllegalArgumentException("A extensão da imagem é obrigatória.");
        }
        if (type == null) {
            throw new IllegalArgumentException("O tipo da imagem é obrigatório.");
        }
    }
}
