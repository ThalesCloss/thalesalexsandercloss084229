package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import java.util.List;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateAlbumCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.GuestDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.TrackDto;
import br.com.tcloss.seletivoseplagapi.application.events.integration.AlbumCreatedIntegrationEvent;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Album;
import br.com.tcloss.seletivoseplagapi.domain.model.album.AlbumRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Guest;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Track;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CreateAlbumCommandHandler {
        private final AlbumRepository albumRepository;
        private final Event<AlbumCreatedIntegrationEvent> integrationEventPublisher;

        @Transactional
        public Album execute(CreateAlbumCommand command) {

                final var album = Album.createNew(
                                command.album().title(),
                                command.album().artistProfileId(),
                                command.album().artistProfileLineupId(),
                                command.album().releaseDate(),
                                this.mapTracks(command.album().tracks()));

                albumRepository.save(album);

                integrationEventPublisher
                                .fire(AlbumCreatedIntegrationEvent.create(album.getId(), album.getTitle().toString()));
                return album;
        }

        private List<Track> mapTracks(List<TrackDto> tracks) {
                return tracks.stream().map(this::createTrack).toList();
        }

        private Track createTrack(TrackDto track) {
                return Track.createNew(
                                track.title(),
                                track.compositionId(),
                                track.trackNumber(),
                                track.discNumber(),
                                track.contexDuration(),
                                track.isrc(),
                                this.mapGuests(track.guests()));
        }

        private List<Guest> mapGuests(List<GuestDto> guests) {
                if (guests == null) {
                        return List.of();
                }
                return guests.stream()
                                .map(guest -> new Guest(guest.id(), guest.creditName(), guest.displayOrder()))
                                .toList();
        }

}
