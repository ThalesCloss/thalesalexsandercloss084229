package br.com.tcloss.seletivoseplagapi.domain.model.album;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ISRCTest {

    @Test
    @DisplayName("Deve criar um ISRC válido")
    public void shouldCreateValidISRC() {
        final var isrc = new ISRC("Br-XXX-23-00001");
        assertThat(isrc).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao criar um ISRC inválido")
    public void shouldFailToCreateInvalidISRC() {
        assertThatThrownBy(() -> {
            new ISRC("Invalid-ISRC");
        }).hasMessageContaining("ISRC inválido: Deve seguir o formato XX-XXX-YY-NNNNN");

    }

}
