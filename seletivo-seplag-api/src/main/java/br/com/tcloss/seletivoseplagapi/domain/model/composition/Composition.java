package br.com.tcloss.seletivoseplagapi.domain.model.composition;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.domain.shared.AggregateRoot;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "compositions")
public class Composition extends AggregateRoot<UUID> {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(
        name = "name",
        column = @Column(name = "title", nullable = false, length = 200)
    )
    private ProperName title;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "iswc", nullable = false, length = 15)
    )
    private ISWC iswc;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "composition_authors",joinColumns = @JoinColumn(name = "composition_id"))
    @Column(name = "author_id", nullable = false)
    private List<UUID> authors = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    public static Composition createNew(String title, String iswc, List<UUID> authors, String lyrics,
            LocalDate releaseDate) {
        final var notification = Notification.create();
        final var iswcValue = notification.tryExecute(() -> iswc != null ? new ISWC(iswc) : null);
        final var properTitle = notification.tryExecute(() -> new ProperName(title));
        notification.validate(authors != null && !authors.isEmpty(), "Deve haver ao menos um autor para a composição.");
        notification.throwIfHasErrors();
        return new Composition(
                UuidCreator.getTimeOrderedEpoch(),
                properTitle,
                iswcValue,
                new ArrayList<>(authors),
                lyrics,
                releaseDate);
    }
}
