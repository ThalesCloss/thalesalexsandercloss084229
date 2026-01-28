package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistProfileDto(
                @NotBlank(message = "Informe o nome artistico (stageName)") @Schema(description = "Nome artístico usado publicamente", examples = {
                                "Legião Urbana", "Leonardo", "Metálica", "Guns N' Roses" }) String stageName,
                @Schema(description = "Biografia do artista") String biography,
                @NotNull(message = "O tipo do artista é obrigatório") @Schema(description = "Tipo de artista") ArtistType artistType,
                @NotNull(message = "Informe pelo menos uma formação") 
                @Schema(description = "Formações do perfil artistico ao longo do tempo")
                List<LineupDto> lineups) {

}
