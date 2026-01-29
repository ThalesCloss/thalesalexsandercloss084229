package br.com.tcloss.seletivoseplagapi.application.dtos.input.album;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrackDto(
                @NotBlank @Schema(description = "Titulo da faixa apresentada no álbum") String title,

                @NotNull @Schema(description = "Identificador da composição da faixa") UUID compositionId,

                @NotNull @Schema(description = "Número do disco no álbum", examples = {
                                "1", "2" }, defaultValue = "1") Integer discNumber,

                @NotNull @Schema(description = "Número da faixa no disco no álbum", examples = { "1", "2",
                                "3" }) Integer trackNumber,

                @NotBlank @Schema(description = "Tempo de duração da faixa no álbum") Duration contexDuration,

                @Schema(description = "ISRC (International Standard Recording Code) da gravação") String isrc,

                @Schema(description = "Convidados da faixa", defaultValue = "[]") @Valid List<GuestDto> guests

        ) {

}
