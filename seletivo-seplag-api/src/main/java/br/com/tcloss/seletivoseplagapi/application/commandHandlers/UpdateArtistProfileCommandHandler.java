package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import br.com.tcloss.seletivoseplagapi.application.commands.UpdateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@ApplicationScoped
public class UpdateArtistProfileCommandHandler {
    private final ArtistProfileRepository artistProfileRepository;

    @Transactional
    public void execute(UpdateArtistProfileCommand updateArtistProfileCommand) {
        final var artistResult = artistProfileRepository.getById(updateArtistProfileCommand.id());
        if (artistResult.isEmpty()) {
            throw new NotFoundException();
        }
        final var artist = artistResult.get();

        artist.update(updateArtistProfileCommand.artistProfileDto().stageName(),
                updateArtistProfileCommand.artistProfileDto().biography());

        artistProfileRepository.save(artist);
    }
}
