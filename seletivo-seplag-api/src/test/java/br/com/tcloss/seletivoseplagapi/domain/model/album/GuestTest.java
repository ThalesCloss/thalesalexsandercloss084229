package br.com.tcloss.seletivoseplagapi.domain.model.album;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class GuestTest {

    @Test
    @DisplayName("Deve criar um convidado válido")
    public void shouldCreateValidGuest() {
        final var guest = new Guest(UUID.fromString("019bf717-7571-7b1d-aebf-efa16913b3c9"),
                "nome do artista creditado", 1);
        assertThat(guest).isNotNull();

    }

    @Test
    @DisplayName("Deve falhar ao criar um convidado sem o id da pessoa")
    public void shouldFailToCreateGuestWithoutPersonId() {
        assertThatThrownBy(() -> {
            new Guest(null, "", 1);
        }).hasMessageContaining("ID da pessoa convidada não pode ser nulo.");
    }

    @Test
    @DisplayName("Deve falhar ao criar um convidado sem o nome do artista")
    public void shouldFailToCreateGuestWithoutArtistName() {
        assertThatThrownBy(() -> {
            new Guest(UUID.fromString("019bf717-7571-7b1d-aebf-efa16913b3c9"), "   ", 1);
        }).hasMessageContaining("Nome de crédito não pode ser vazio.");
    }
}
