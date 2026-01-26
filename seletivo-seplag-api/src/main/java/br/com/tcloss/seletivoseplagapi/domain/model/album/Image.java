package br.com.tcloss.seletivoseplagapi.domain.model.album;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;

public record Image(String identifier, String extension, ImageType type) implements ValueObject {

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
