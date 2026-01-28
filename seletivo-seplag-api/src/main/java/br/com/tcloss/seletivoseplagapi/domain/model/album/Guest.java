package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Guest(
    @Column(name = "person_id", nullable = false)
    UUID personId, 
    @Column(name = "credit_name", nullable = false)
    String creditName, 
    @Column(name = "display_order", nullable = false)
    Integer displayOrder) implements ValueObject {
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
