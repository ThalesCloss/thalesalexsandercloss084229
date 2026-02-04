package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public record UpdateArtistProfileDto(
                @NotBlank(message = "Informe o nome artistico (stageName)") @Schema(description = "Nome artístico usado publicamente", examples = {
                                "Legião Urbana", "Leonardo", "Metálica", "Guns N' Roses" }) String stageName,
                @Schema(description = "Biografia do artista")
                String biography) {

}
