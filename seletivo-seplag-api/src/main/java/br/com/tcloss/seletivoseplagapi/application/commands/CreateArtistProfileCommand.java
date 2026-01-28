package br.com.tcloss.seletivoseplagapi.application.commands;


import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;
import jakarta.validation.constraints.NotNull;

public record CreateArtistProfileCommand(
        @NotNull() String stageName,
        String biography,
        ArtistType artistType

) {

}
