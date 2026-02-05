package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import br.com.tcloss.seletivoseplagapi.application.commands.CreateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfile;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Lineup;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Member;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CreateArtistProfileCommandHandler {
    final private ArtistProfileRepository artistProfileRepository;

    @Transactional
    public ArtistProfile execute(CreateArtistProfileCommand request) {
        final var artist = ArtistProfile.createNew(
                request.artistProfile().stageName(),
                request.artistProfile().biography(),
                request.artistProfile().artistType(),
                request.artistProfile().lineups().stream()
                        .map(lineup -> Lineup.createNew(lineup.label(), lineup.members().stream().map(
                                member -> new Member(member.personId(), member.isMainArtist(), member.isFounder()))
                                .toList(),
                                lineup.startDate(), lineup.endDate()))
                        .toList());
        artistProfileRepository.save(artist);
        return artist;
    }
}
