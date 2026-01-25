package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineupTest {

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar membro duplicado na formação")
    void shouldThrowExceptionWhenAddingDuplicateMember() {

        final List<UUID> members = List.of(UUID.fromString("019bf664-4f4f-7b6e-990e-c7258097d113"),
                UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"),
                UUID.fromString("019bf665-d3f9-735c-a27a-280db86ead91"),
                UUID.fromString("019bf664-4f4f-7b6e-990e-c7258097d113"),
                UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"));

        assertThatThrownBy(() -> {
            Lineup.createNew("Banda X", members.stream().map(m -> new Member(m, false, false)).toList(),
                    LocalDate.of(2000, 1, 1), null);
        }).isInstanceOf(DomainException.class).extracting(e -> ((DomainException) e).getErrors()).asList()
                .hasSize(2)
                .containsExactly("Membro 019bf664-4f4f-7b6e-990e-c7258097d113 já existe na formação",
                        "Membro 019bf665-07fd-7386-bb3e-702b48dbbed6 já existe na formação");

    }

    @Test
    @DisplayName("Deve criar formação com sucesso")
    void shouldCreateLineupSuccessfully() {
        final Lineup lineup = Lineup.createNew("Banda X",
                new ArrayList<Member>(List.of(
                        new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), false, false),
                        new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14b3"), true, true))),
                LocalDate.of(2000, 1, 1), null);

        assertThat(lineup).isNotNull();
        assertThat(lineup.getLabel().toString()).isEqualTo("Banda X");
        assertThat(lineup.getMembers()).hasSize(2);
        assertThat(lineup.getDuration().startDate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(lineup.getDuration().endDate()).isNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar formação sem artista principal")
    void shouldThrowExceptionWhenCreatingLineupWithoutMainArtist() {
        assertThatThrownBy(() -> {
            Lineup.createNew("Banda X",
                    new ArrayList<Member>(List.of(
                            new Member(UUID.fromString("019bf665-07fd-7386-bb3e-702b48dbbed6"), false, false),
                            new Member(UUID.fromString("019bf673-09bc-7ff3-8f37-284a6bef14b3"), false, true))),
                    LocalDate.of(2000, 1, 1), null);
        }).isInstanceOf(DomainException.class).extracting(e -> ((DomainException) e).getErrors()).asList()
                .hasSize(1)
                .containsExactly("A formação deve ter ao menos um artista principal");
    }
}