package br.com.tcloss.seletivoseplagapi.application.commandHandlers;

import br.com.tcloss.seletivoseplagapi.application.commands.CreateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CreateArtistProfileCommandHandler {
    final private ArtistProfileRepository artistProfileRepository;

    @Transactional
    public void execute(CreateArtistProfileCommand request){

    }
}
