package br.com.tcloss.seletivoseplagapi.application.dtos.input.album;

import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GuestDto(
        @NotNull @Schema(description = "Id da pessoa convidada") UUID id,

        @NotEmpty @Schema(description = "Nome da participação a ser exibido nos créditos") String creditName,

        @NotNull @Schema(description = "Ordem de exibição") Integer displayOrder) {

}
