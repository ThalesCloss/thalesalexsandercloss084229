package br.com.tcloss.seletivoseplagapi.domain.model.composition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;

public class CompositionTest {

    @Test
    @DisplayName("Deve criar uma composição válida")
    public void shouldCreateValidComposition() {
        assertThat(
                Composition.createNew(
                        "Composicao", null,
                        List.of(UUID.fromString("019bf717-7571-7b1d-aebf-efa16913b3c9")),
                        null,
                        LocalDate.of(2026, 01, 25)))
                .isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao criar uma composição com dados inválidos")
    public void shouldFailToCreateCompositionWithInvalidData() {
        assertThatThrownBy(() -> {
            Composition.createNew("O", "daskj", null, null, null);
        })
                .isInstanceOf(DomainException.class).extracting(e -> ((DomainException) e).getErrors()).asList()
                .hasSize(3)
                .containsExactlyInAnyOrder(
                        "Nome deve ter pelo menos 2 caracteres",
                        "ISWC inválido. Formato esperado: T-123.456.789-0",
                        "Deve haver ao menos um autor para a composição.");
    }
}
