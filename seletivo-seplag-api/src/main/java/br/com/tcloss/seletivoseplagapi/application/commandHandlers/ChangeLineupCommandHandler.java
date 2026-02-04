package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import br.com.tcloss.seletivoseplagapi.application.commands.ChangeLineupCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@ApplicationScoped
public class ChangeLineupCommandHandler {
    private final ArtistProfileRepository artistProfileRepository;

    @Transactional
    public void execute(ChangeLineupCommand changeLineupCommand) {
        final var artistResult = artistProfileRepository.getById(changeLineupCommand.artistId());
        if (artistResult.isEmpty()) {
            throw new NotFoundException();
        }
        final var artist = artistResult.get();

        artist.changeLineup(
                changeLineupCommand.lineup().label(), changeLineupCommand.lineup().members().stream().map(
                        member -> new Member(member.personId(), member.isMainArtist(), member.isFounder()))
                        .toList(),
                changeLineupCommand.lineup().startDate());
                
        artistProfileRepository.save(artist);
    }
}
