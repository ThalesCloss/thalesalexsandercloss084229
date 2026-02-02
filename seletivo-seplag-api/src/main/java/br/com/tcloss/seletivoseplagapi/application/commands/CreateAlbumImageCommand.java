package br.com.tcloss.seletivoseplagapi.application.commands;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.ImageDto;

public record CreateAlbumImageCommand(
    UUID albumId,
    ImageDto image
) {

}
