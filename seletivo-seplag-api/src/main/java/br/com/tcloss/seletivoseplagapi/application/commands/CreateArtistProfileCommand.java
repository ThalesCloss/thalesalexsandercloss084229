package br.com.tcloss.seletivoseplagapi.application.commands;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileDto;

public record CreateArtistProfileCommand(
                ArtistProfileDto artistProfile) {

}
