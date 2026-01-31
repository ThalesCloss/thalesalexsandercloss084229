package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import br.com.tcloss.seletivoseplagapi.application.commands.CreateAlbumCommand;
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
        public void execute(CreateAlbumCommand command) {
                final var album = Album.createNew(
                                command.album().title(),
                                command.album().artistProfileId(),
                                command.album().artistProfileLineupId(),
                                command.album().releaseDate(),
                                command.album().tracks().stream().map(track -> Track.createNew(
                                                track.title(),
                                                track.compositionId(),
                                                track.trackNumber(),
                                                track.discNumber(),
                                                track.contexDuration(),
                                                track.isrc(),
                                                track.guests().stream()
                                                                .map(guest -> new Guest(guest.id(), guest.creditName(),
                                                                                guest.displayOrder()))
                                                                .toList()))
                                                .toList(),
                                null);
                albumRepository.save(album);
                integrationEventPublisher
                                .fire(AlbumCreatedIntegrationEvent.create(album.getId(), album.getTitle().toString()));
        }

}
