package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ArtistProfileTest {

    @Test
    @DisplayName("Deve criar um artista solo com sucesso")
    void shouldCreateSoloArtistSuccessfully() {
        final var lineup = Lineup.createNew("Solo Artist", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true))),
                LocalDate.of(2020, 1, 1), null);
        final var artist = ArtistProfile.createNew("Solo Artist", "A great solo artist", ArtistType.SOLO, List.of(lineup));
        assertThat(artist).isNotNull();
        assertThat(artist.getArtistType()).isEqualTo(ArtistType.SOLO);
        assertThat(artist.getLineups()).hasSize(1);
    }

    @Test
    @DisplayName("Não deve criar um artista solo com mais de um membro")
    void shouldNotCreateSoloArtistWithMoreThanOneMember() {
        final var lineup = Lineup.createNew("Solo Artist", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true),
                new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14b3"), false, false))),
                LocalDate.of(2020, 1, 1), null);

        assertThatThrownBy(() -> ArtistProfile.createNew("Solo Artist", "A great solo artist", ArtistType.SOLO, List.of(lineup)))
                .hasMessageContaining("A formação de um artista solo deve conter exatamente um membro.");

    }

    @Test
    @DisplayName("Deve criar um artista duo com sucesso")
    void shouldCreateDuoArtistSuccessfully() {
        final var lineup = Lineup.createNew("Duo Artist", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true),
                new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14b3"), false, false))),
                LocalDate.of(2020, 1, 1), null);
        final var artist = ArtistProfile.createNew("Duo Artist", "A great duo artist", ArtistType.DUO, List.of(lineup));
        assertThat(artist).isNotNull();
        assertThat(artist.getArtistType()).isEqualTo(ArtistType.DUO);
        assertThat(artist.getLineups()).hasSize(1);
        assertThat(artist.getCurrentLineup().getMembers()).hasSize(2);
    }

    @Test
    @DisplayName("Não deve criar um artista duo com quantidade de membros diferente de dois")
    void shouldNotCreateDuoArtistWithWrongNumberOfMembers() {
        final var lineup = Lineup.createNew("Duo Artist", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true))),
                LocalDate.of(2020, 1, 1), null);

        assertThatThrownBy(() -> ArtistProfile.createNew("Duo Artist", "A great duo artist", ArtistType.DUO,List.of(lineup)))
                .hasMessageContaining("A formação de um artista duo deve conter exatamente dois membros.");
    }

    @Test
    @DisplayName("Deve criar uma banda com sucesso")
    void shouldCreateBandSuccessfully() {
        final var lineup = Lineup.createNew("Band", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true),
                new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14b3"), false, false),
                new Member(UUID.fromString("019bf674-0a1c-7f4e-9d2e-385a6bef14c4"), false, false),
                new Member(UUID.fromString("019bf675-1b2d-8f5f-ad3f-486b7cef25d5"), false, false))),
                LocalDate.of(2020, 1, 1), null);
        final var artist = ArtistProfile.createNew("Band", "A great band", ArtistType.BAND, List.of(lineup));
        assertThat(artist).isNotNull();
        assertThat(artist.getArtistType()).isEqualTo(ArtistType.BAND);
        assertThat(artist.getLineups()).hasSize(1);
        assertThat(artist.getCurrentLineup().getMembers()).hasSize(4);
    }

    @Test
    @DisplayName("Não deve permitir a alteração de formação de artista solo após a criação")
    void shouldNotAllowChangingLineupAfterCreation() {
        final var lineup = Lineup.createNew("Solo Artist", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true))),
                LocalDate.of(2020, 1, 1), null);
        final var artist = ArtistProfile.createNew("Solo Artist", "A great solo artist", ArtistType.SOLO, List.of(lineup));

        assertThatThrownBy(() -> artist.changeLineup("New Solo Artist", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true))),
                LocalDate.of(2021, 1, 1)))
                .hasMessageContaining("Artistas solo não podem alterar sua formação após a criação inicial.");
    }

    @Test
    @DisplayName("Deve permitir a alteração de formação e finalizar a formação anterior")
    void shouldAllowChangingLineupAndFinalizePreviousLineup() {
        final var lineup = Lineup.createNew("Dupla", new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true),
                new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14b3"), false, false))),
                LocalDate.of(2020, 1, 1), null);
        final var artist = ArtistProfile.createNew("Dupla", "A great duo", ArtistType.DUO, List.of(lineup));

        final var novaFormacao = "Nova formação";
        final var dataInicio = LocalDate.of(2021, 1, 1);
        artist.changeLineup(novaFormacao, new ArrayList<Member>(List.of(
                new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), true, true),
                new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14c3"), false, false))),
                dataInicio);

        assertThat(artist.getCurrentLineup().getLabel().toString()).isEqualTo("Nova formação");
        assertThat(artist.getLineups().get(0).getDuration().endDate().isEqual(dataInicio));
    }

}