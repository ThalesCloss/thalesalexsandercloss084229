package br.com.tcloss.seletivoseplagapi.application.dtos.input.album;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AlbumDto(
                @NotBlank @Schema(description = "Nome do album") String title,

                @NotNull @Schema(description = "Id do perfil artistico responsável pelo album") UUID artistProfileId,

                @NotNull @Schema(description = "Id da formação do perfil artistico responsável pelo album") UUID artistProfileLineupId,

                @NotNull @Schema(description = "Data de lançamento do album") LocalDate releaseDate,

                @Schema(description = "Faixas do álbum") @NotEmpty List<@Valid TrackDto> tracks

) {

}
