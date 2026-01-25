package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.time.LocalDate;
import java.util.ArrayList;
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
public class ArtistProfile extends AggregateRoot<UUID>{
    private UUID id;
    private ProperName stageName;
    private String biography;
    private ArtistType artistType;
    private List<Lineup> lineups = new ArrayList<>();


    public void changeLineup(String label, List<Member> members, LocalDate startedAt) {
        final var notification = Notification.create();
        final var newLabel = notification.tryExecute(()-> new ProperName(label));
        final var newLineup = notification.tryExecute(()-> Lineup.createNew(newLabel.toString(), members, startedAt, null));
        notification.tryExecute(()-> this.artistType.validate(this, newLineup));
        notification.throwIfHasErrors();
        this.closeCurrentLineup(startedAt);
        this.lineups.add(newLineup);

    }

    private void closeCurrentLineup(LocalDate endedAt) {
        final var currentLineup = this.getCurrentLineup();
        if (currentLineup != null) {
            currentLineup.finishLineup(endedAt);
        }
    }

    

    public Lineup getCurrentLineup() {
        return this.lineups.stream()
                .filter(lineup -> lineup.getDuration().endDate() == null)
                .findFirst()
                .orElse(null);
    }

    public static ArtistProfile createNew(String stageName, String biography, ArtistType artistType, Lineup initialLineup) {
        final var notification = Notification.create();
        final var properStageName = notification.tryExecute(()-> new ProperName(stageName));
        final var artist = new ArtistProfile(UUID.randomUUID(), properStageName, biography, artistType, new ArrayList<>());
        notification.tryExecute(()-> artistType.validate(artist, initialLineup));
        notification.throwIfHasErrors();
        artist.lineups.add(initialLineup);
        return artist;
    }
}
