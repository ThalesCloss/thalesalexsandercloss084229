package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;

public class ArtistTypeTest {

    @Test
    @DisplayName("SOLO: Deve falhar se a formação tiver mais de um membro")
    void shouldFailIfLineupHasMoreThanOneMember() {
        final var lineup = fakeLineupWithMembers(2);
        final var artist = Mockito.mock(ArtistProfile.class);
        Mockito.when(artist.getLineups()).thenReturn(new ArrayList<>());
        assertThatThrownBy(() -> {
            ArtistType.SOLO.validate(artist, lineup);
        }).isInstanceOf(DomainException.class).hasMessageContaining("A formação de um artista solo deve conter exatamente um membro.");
    }

    @Test
    @DisplayName("SOLO: Deve falhar se tentar alterar a formação após a criação")
    void shouldFailIfChangingLineupAfterCreation() {
        final var lineup = fakeLineupWithMembers(1);
        final var artist = Mockito.mock(ArtistProfile.class);
        Mockito.when(artist.getLineups()).thenReturn(List.of(lineup));
        assertThatThrownBy(() -> {
            ArtistType.SOLO.validate(artist, lineup);
        }).isInstanceOf(DomainException.class).hasMessageContaining("Artistas solo não podem alterar sua formação após a criação inicial.");
    }

    @Test
    @DisplayName("DUO: Deve falhar se a formação tiver menos de dois membros")
    void shouldFailIfLineupHasLessThanTwoMembers() {
        final var lineup = fakeLineupWithMembers(1);
        final var artist = Mockito.mock(ArtistProfile.class);
        assertThatThrownBy(() -> {
            ArtistType.DUO.validate(artist, lineup);
        }).isInstanceOf(DomainException.class).hasMessageContaining("A formação de um artista duo deve conter exatamente dois membros.");
    }
    @Test
    @DisplayName("DUO: Deve falhar se a formação tiver mais de dois membros")
    void shouldFailIfLineupHasMoreThanTwoMembers() {
        final var lineup = fakeLineupWithMembers(3);
        final var artist = Mockito.mock(ArtistProfile.class);
        assertThatThrownBy(() -> {
            ArtistType.DUO.validate(artist, lineup);
        }).isInstanceOf(DomainException.class).hasMessageContaining("A formação de um artista duo deve conter exatamente dois membros.");
    }

    @Test
    @DisplayName("BAND: Deve falhar se a formação tiver menos de quatro membros")
    void shouldFailIfBandLineupHasLessThanTwoMembers() {
        final var lineup = fakeLineupWithMembers(3);
        final var artist = Mockito.mock(ArtistProfile.class);
        assertThatThrownBy(() -> {
            ArtistType.BAND.validate(artist, lineup);
        }).isInstanceOf(DomainException.class).hasMessageContaining("A formação deve conter pelo menos 4 membros.");
    }

    private Lineup fakeLineupWithMembers(int memberCount) {
        List<Member> members = new ArrayList();
        for (int i = 0; i < memberCount; i++) {
            members.add(new Member(UuidCreator.getTimeOrderedEpoch(), i == 0, false));
        }
        return Lineup.createNew("Fake Lineup", members, LocalDate.now(), null);
    }
}