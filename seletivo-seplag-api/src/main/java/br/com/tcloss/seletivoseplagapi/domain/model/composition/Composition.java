package br.com.tcloss.seletivoseplagapi.domain.model.composition;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.AggregateRoot;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Composition extends AggregateRoot<UUID> {
    private UUID id;

    private ProperName title;

    private ISWC iswc;

    private List<UUID> authors;

    private String lyrics;

    private LocalDate releaseDate;

    public static Composition createNew(String title, String iswc, List<UUID> authors, String lyrics,
            LocalDate releaseDate) {
        final var notification = Notification.create();
        final var iswcValue = notification.tryExecute(() -> iswc != null ? new ISWC(iswc) : null);
        final var properTitle = notification.tryExecute(() -> new ProperName(title));
        notification.validate(authors!=null && !authors.isEmpty(), "Deve haver ao menos um autor para a composição.");
        notification.throwIfHasErrors();
        return new Composition(
                UUID.randomUUID(),
                properTitle,
                iswcValue,
                List.copyOf(authors),
                lyrics,
                releaseDate);
    }
}
