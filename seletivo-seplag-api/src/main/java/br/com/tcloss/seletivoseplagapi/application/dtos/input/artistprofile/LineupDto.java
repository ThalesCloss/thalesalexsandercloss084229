package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LineupDto(
    @NotBlank(message = "O nome da formação deve ser informado")
    String label,

    @NotNull(message = "A data de inicio da formação é obrigatória")
    LocalDate startDate,

    LocalDate endDate,

    @NotEmpty(message = "Informe os membros da formação")
    @Valid
    List<MemberDto> members
) {

}
