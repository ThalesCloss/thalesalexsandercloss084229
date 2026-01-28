package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record MemberDto(
    @NotNull(message = "O ID da pessoa é obrigatório")
    UUID personId,
    boolean isMainArtist,
    boolean isFounder
) {

}
