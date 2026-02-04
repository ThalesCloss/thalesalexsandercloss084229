package br.com.tcloss.seletivoseplagapi.application.commands;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.LineupDto;

public record ChangeLineupCommand(
    UUID artistId,
    LineupDto lineup
) {

}
