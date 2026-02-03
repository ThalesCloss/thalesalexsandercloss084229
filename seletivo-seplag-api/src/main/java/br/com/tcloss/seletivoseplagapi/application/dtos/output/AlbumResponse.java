package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.time.LocalDate;
import java.util.UUID;

public record AlbumResponse(
    UUID albumId,
    String title,
    UUID artistId,
    String artistName,
    LocalDate releaseDate,
    Integer tracks,
    ImageResponse image
) {

}
