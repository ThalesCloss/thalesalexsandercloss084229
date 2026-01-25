package br.com.tcloss.seletivoseplagapi.domain.model.composition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ISWCTest {

    @Test
    @DisplayName("Deve criar um ISWC válido")
    public void shouldCreateValidISWC() {
        final var iswc = new ISWC("T-032.232.233-8");
        assertThat(iswc).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao criar um ISWC inválido")
    public void shouldFailToCreateInvalidISWC() {
        assertThatThrownBy(()->{
            new ISWC("Y-dd2.333.222.d");
        })
        .hasMessageContaining("ISWC inválido. Formato esperado: T-123.456.789-0");
        
    }

    @Test
    @DisplayName("Deve falhar ao criar um ISWC nulo")
    public void shouldFailToCreateNullISWC() {
         assertThatThrownBy(()->{
            new ISWC(null);
        })
        .hasMessageContaining("ISWC não pode ser vazio");
    }
}
