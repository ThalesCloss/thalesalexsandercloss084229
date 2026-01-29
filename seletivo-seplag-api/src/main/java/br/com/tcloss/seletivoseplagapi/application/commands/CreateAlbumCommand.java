package br.com.tcloss.seletivoseplagapi.application.commands;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumDto;

public record CreateAlbumCommand(
    AlbumDto album
) {

}
