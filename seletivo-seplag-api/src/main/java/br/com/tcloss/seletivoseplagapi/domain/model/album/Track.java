package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.domain.shared.BaseEntity;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;
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
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "album_tracks")
public class Track extends BaseEntity<UUID> {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @AttributeOverride(name = "name", column = @Column(name = "title", nullable = false, length = 200))
    @Embedded
    private ProperName title;

    @Column(name = "composition_id", nullable = false)
    private UUID compositionId;

    @Column(name = "track_number", nullable = false)
    private Integer trackNumber;

    @Column(name = "disc_number", nullable = false)
    private Integer discNumber;

    @Column(name = "duration", nullable = false)
    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration contexDuration;

    @Embedded
    @AttributeOverride(name = "isrc", column = @Column(name = "isrc", length = 15))
    private ISRC isrc;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "album_track_guests", 
        joinColumns = @JoinColumn(name = "track_id")
    )
    @OrderBy("displayOrder ASC")
    private List<Guest> guests;

    public void addGuest(Guest newGuest) {
        boolean alreadyAdded = guests.stream()
                .anyMatch(guest -> guest.personId().equals(newGuest.personId()));

        if (alreadyAdded) {
            throw new DomainException("Este artista já é um convidado nesta faixa");
        }

        this.guests.add(newGuest);
    }

    public void removeGuest(UUID personId) {
        boolean removed = guests.removeIf(guest -> guest.personId().equals(personId));

        if (!removed) {
            throw new DomainException("Este artista não é um convidado nesta faixa");
        }
    }

    public String getFullTitle() {
        if (guests.isEmpty()) {
            return title.toString();
        }
        String feats = guests.stream()
                .map(Guest::creditName)
                .collect(Collectors.joining("/ "));

        return "%s (feat. %s)".formatted(title.toString(), feats);
    }

    public static Track createNew(String title, UUID compositionId, Integer trackNumber, Integer discNumber,
            Duration contexDuration, String isrc, List<Guest> guests) {
        final var notification = Notification.create();
        final var properTitle = notification.tryExecute(() -> new ProperName(title));
        final var isrcValue = notification.tryExecute(() -> isrc != null ? new ISRC(isrc) : null);
        final var track = new Track(UuidCreator.getTimeOrderedEpoch(), properTitle, compositionId, trackNumber,
                discNumber,
                contexDuration, isrcValue, new ArrayList<>());
        if (guests != null) {
            guests.forEach((guest) -> {
                notification.tryExecute(() -> {
                    track.addGuest(guest);
                });
            });
        }
        notification.throwIfHasErrors();
        return track;
    }
}
