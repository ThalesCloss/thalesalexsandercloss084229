package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import java.util.List;

import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistProfileDto(
        @NotBlank(message = "Informe o nome artistico (stageName)") String stageName,
        String biography,
        @NotNull(message = "O tipo do artista é obrigatório") ArtistType artistType,
        @NotNull(message = "Informe pelo menos uma formação") List<LineupDto> lineups) {

}
