package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.tcloss.seletivoseplagapi.domain.shared.BaseEntity;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Track extends BaseEntity<UUID> {
    private UUID id;
    private ProperName title;
    private UUID compositionId;
    private Integer trackNumber;
    private Integer discNumber;
    private Duration contexDuration;
    private ISRC isrc;
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
        final var track = new Track(UUID.randomUUID(), properTitle, compositionId, trackNumber, discNumber,
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
