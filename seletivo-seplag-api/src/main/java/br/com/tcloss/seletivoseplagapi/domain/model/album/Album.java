package br.com.tcloss.seletivoseplagapi.domain.model.album;

import java.time.LocalDate;
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
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "albums")
public class Album extends AggregateRoot<UUID> {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @AttributeOverride(name = "name", column = @Column(name = "title", nullable = false, length = 200))
    @Embedded
    private ProperName title;

    @Column(name = "artist_profile_id", nullable = false)
    private UUID artistProfileId;

    @Column(name = "artist_profile_lineup_id", nullable = false)

    private UUID artistProfileLineupId;

    @Column(name = "start_date", nullable = false)
    private LocalDate releaseDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id", nullable = false, updatable = false)
    @OrderBy("disk_number, track_number")
    @Fetch(FetchMode.SUBSELECT)
    private List<Track> tracks;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "album_images", joinColumns = @JoinColumn(name = "album_id"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Image> images;

    public void addImage(Image newImage) {
        this.images.add(newImage);
    }

    public void removeImage(String identifier) {
        boolean removed = this.images.removeIf(image -> image.identifier().equals(identifier));

        if (!removed) {
            throw new IllegalArgumentException("Imagem não encontrada no álbum.");
        }
    }

    public void addTrack(Track newTrack) {
        this.tracks.add(newTrack);
    }

    public void removeTrack(UUID trackId) {
        boolean removed = this.tracks.removeIf(track -> track.getId().equals(trackId));

        if (!removed) {
            throw new IllegalArgumentException("Faixa não encontrada no álbum.");
        }
    }

    public static Album createNew(
            String title,
            UUID artistProfileId,
            UUID artistProfileLineupId,
            LocalDate releaseDate,
            List<Track> tracks) {
        final var notification = Notification.create();
        final var properTitle = notification.tryExecute(() -> new ProperName(title));
        notification.validate(artistProfileId != null && !artistProfileId.toString().isBlank(),
                "O ID do perfil do artista é obrigatório.");
        notification.validate(artistProfileLineupId != null && !artistProfileLineupId.toString().isBlank(),
                "O ID da formação do perfil do artista é obrigatório.");
        notification.validate(tracks != null && !tracks.isEmpty(), "O album deve conter ao menos uma faixa.");
        notification.throwIfHasErrors();
        return new Album(
                UuidCreator.getTimeOrderedEpoch(),
                properTitle,
                artistProfileId,
                artistProfileLineupId,
                releaseDate,
                List.copyOf(tracks),
                List.of());
    }

}
