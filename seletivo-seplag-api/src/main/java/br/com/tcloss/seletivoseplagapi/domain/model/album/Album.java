package br.com.tcloss.seletivoseplagapi.domain.model.album;

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
public class Album extends AggregateRoot<UUID> {

    private UUID id;

    private ProperName title;

    private UUID artistProfileId;

    private UUID artistProfileLineupId;

    private LocalDate releaseDate;

    private List<Track> tracks;

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
            List<Track> tracks,
            List<Image> images) {
        final var notification = Notification.create();
        final var properTitle = notification.tryExecute(() -> new ProperName(title));
        notification.validate(artistProfileId != null && !artistProfileId.toString().isBlank(),
                "O ID do perfil do artista é obrigatório.");
        notification.validate(artistProfileLineupId != null && !artistProfileLineupId.toString().isBlank(),
                "O ID da formação do perfil do artista é obrigatório.");
        notification.validate(tracks != null && !tracks.isEmpty(), "O album deve conter ao menos uma faixa.");
        notification.throwIfHasErrors();
        return new Album(
                UUID.randomUUID(),
                properTitle,
                artistProfileId,
                artistProfileLineupId,
                releaseDate,
                List.copyOf(tracks),
                images != null ? List.copyOf(images) : List.of());
    }

}
