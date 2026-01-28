package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

public record MemberDto(
    @NotNull(message = "O ID da pessoa é obrigatório")
    @Schema(description = "Identificador da pessoa presente na formação")
    UUID personId,
    @Schema(description = "Indica se é um artista principal")
    boolean isMainArtist,
    @Schema(description = "Indica se é um fundador")
    boolean isFounder
) {

}
