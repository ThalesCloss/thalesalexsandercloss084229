package br.com.tcloss.seletivoseplagapi.domain.model.artistprofile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.domain.shared.AggregateRoot;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "artist_profiles")
public class ArtistProfile extends AggregateRoot<UUID> {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @AttributeOverride(name = "name", column = @Column(name = "stage_name", nullable = false, length = 200))
    @Embedded
    private ProperName stageName;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Enumerated(EnumType.STRING)
    @Column(name = "artist_type", nullable = false, length = 20)
    private ArtistType artistType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_profile_id", nullable = false)
    @Fetch(FetchMode.SUBSELECT)
    private List<Lineup> lineups = new ArrayList<>();

    public void changeLineup(String label, List<Member> members, LocalDate startedAt) {
        final var notification = Notification.create();
        final var newLabel = notification.tryExecute(() -> new ProperName(label));
        final var newLineup = notification
                .tryExecute(() -> Lineup.createNew(newLabel.toString(), members, startedAt, null));
        notification.tryExecute(() -> this.artistType.validate(this, newLineup));
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

    public static ArtistProfile createNew(String stageName, String biography, ArtistType artistType,
            List<Lineup> lineups) {
        final var notification = Notification.create();
        final var properStageName = notification.tryExecute(() -> new ProperName(stageName));
        final var artist = new ArtistProfile(UuidCreator.getTimeOrderedEpoch(), properStageName, biography, artistType,
                new ArrayList<>());
        notification.validate(lineups != null && !lineups.isEmpty(), "O perfil deve ter pelo menos uma formação");
        if (lineups != null) {
            lineups.forEach(lineup -> {
                notification.tryExecute(() -> artistType.validate(artist, lineup));
                artist.lineups.add(lineup);
            });
        }
        notification.throwIfHasErrors();
        return artist;
    }
}
