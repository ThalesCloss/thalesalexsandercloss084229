package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;

public record Guest(UUID personId, String creditName, Integer displayOrder) implements ValueObject {
    public Guest {
        if (personId == null) {
            throw new IllegalArgumentException("ID da pessoa convidada não pode ser nulo.");
        }
        if (creditName == null || creditName.isBlank()) {
            throw new IllegalArgumentException("Nome de crédito não pode ser vazio.");
        }
        displayOrder = displayOrder != null ? displayOrder : 0;
    }
}
