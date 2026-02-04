package br.com.tcloss.seletivoseplagapi.application.commands;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.UpdateArtistProfileDto;

public record UpdateArtistProfileCommand(
    UUID id,
    UpdateArtistProfileDto artistProfileDto
) {

}
