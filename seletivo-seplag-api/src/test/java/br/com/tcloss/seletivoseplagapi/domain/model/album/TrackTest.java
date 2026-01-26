package br.com.tcloss.seletivoseplagapi.domain.model.album;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TrackTest {

    @Test
    @DisplayName("Deve criar uma faixa com convidados válida")
    public void shouldCreateValidTrackWithGuests() {
        final var trackName = "Nome da Faixa";
        final var creditName1 = "Nome do artista 1";
        final var creditName2 = "Nome do artista 2";
        final var track = Track.createNew(
                trackName, UUID.fromString("019bf7d3-1acb-71a4-96ab-978fd3045efb"), 1, 1, Duration.ofMinutes(3),
                "BR-XXX-23-00001",
                List.of(new Guest(UUID.fromString("019bf7d4-b00d-7669-b9a8-ac7b980d43c3"), creditName1, 1),
                        new Guest(UUID.fromString("019bf7d7-3ec0-73c2-ab2d-1acf8fea7ef7"), creditName2, 2)));

        assertThat(track).isNotNull();
        assertThat(track.getFullTitle()).isEqualTo("%s (feat. %s/ %s)".formatted(trackName, creditName1, creditName2));
    }

    @Test
    @DisplayName("Deve criar uma faixa sem convidados válida")
    public void shouldCreateValidTrackWithoutGuests() {
        final var track = Track.createNew(
                "Nome da Faixa", UUID.fromString("019bf7d3-1acb-71a4-96ab-978fd3045efb"), 1, 1, Duration.ofMinutes(3),
                "BR-XXX-23-00001", null);
        assertThat(track).isNotNull();
        assertThat(track.getFullTitle()).isEqualTo("Nome da Faixa");
    }

    @Test
    @DisplayName("Não deve criar uma faixa com convidados repetidos")
    public void shouldNotCreateTrackWithDuplicateGuests() {
        final var guestId = UUID.fromString("019bf7d4-b00d-7669-b9a8-ac7b980d43c3");
        final var guest1 = new Guest(guestId, "Nome do artista", 1);
        final var guest2 = new Guest(guestId, "Nome do artista", 2);
        assertThatThrownBy(() -> {
            Track.createNew(
                    "Nome da Faixa", UUID.fromString("019bf7d3-1acb-71a4-96ab-978fd3045efb"), 1, 1,
                    Duration.ofMinutes(3),
                    "BR-XXX-23-00001",
                    List.of(guest1, guest2));
        }).hasMessageContaining("Este artista já é um convidado nesta faixa");
    }
}
