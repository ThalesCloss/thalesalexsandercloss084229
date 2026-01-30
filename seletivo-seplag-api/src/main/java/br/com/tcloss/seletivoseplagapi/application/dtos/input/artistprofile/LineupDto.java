package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LineupDto(
        @NotBlank(message = "O nome da formação deve ser informado") @Schema(description = "Rótulo que identifica a formação", examples = {
                "Formação original", "Segunda formação" }) String label,

        @NotNull(message = "A data de inicio da formação é obrigatória") @Schema(description = "Data em que a formação foi estabelecida") LocalDate startDate,

        @Schema(description = "Para formações já encerradas, a data em que ocorreu o encerramento", required = false) LocalDate endDate,

        @NotEmpty(message = "Informe os membros da formação")

        @Schema(description = "Membros presentes na formação") List<@Valid MemberDto> members) {

}
