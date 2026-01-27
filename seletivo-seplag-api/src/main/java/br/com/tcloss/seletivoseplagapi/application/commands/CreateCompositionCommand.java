package br.com.tcloss.seletivoseplagapi.application.commands;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CreateCompositionCommand(
        @NotBlank String title,
        @NotBlank String iswc,
        @NotEmpty List<UUID> authorIds,
        String lyrics,
        @NotBlank LocalDate releaseDate) {

}
