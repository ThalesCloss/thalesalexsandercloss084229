package br.com.tcloss.seletivoseplagapi.domain.model.album;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AlbumTest {

    @Test
    @DisplayName("Deve criar um álbum válido")
    public void shouldCreateValidAlbum() {
        final var track = Mockito.mock(Track.class);
        final var album = Album.createNew(
                "Nome do Álbum",
                UUID.fromString("019bf7d3-1acb-71a4-96ab-978fd3045efb"),
                UUID.fromString("019bf7eb-b2f8-76a2-88ae-7372731b00d0"),
                LocalDate.of(2000, 10, 01),
                List.of(track));
        assertThat(album).isNotNull();
    }

    @Test
    @DisplayName("Não deve criar um album sem faixa")
    public void shouldNotCreateAlbumWithoutTracks() {
        assertThatThrownBy(() -> {
            Album.createNew(
                    "Nome do Álbum",
                    null,
                    null,
                    LocalDate.of(2000, 10, 01),
                    List.of());
        }).hasMessageContaining("O album deve conter ao menos uma faixa.");

    }

    @Test
    @DisplayName("Não deve criar um album sem artista e sem a formação")
    public void shouldNotCreateAlbumWithoutArtistAndLineup() {
        final var track = Mockito.mock(Track.class);
        assertThatThrownBy(() -> {
            Album.createNew(
                    "Nome do Álbum",
                    null,
                    null,
                    LocalDate.of(2000, 10, 01),
                    List.of(track));
        }).hasMessageContaining("O ID do perfil do artista é obrigatório.")
                .hasMessageContaining("O ID da formação do perfil do artista é obrigatório.");
    }

}