package br.com.tcloss.seletivoseplagapi.application.commands;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePersonCommand(
        @NotBlank(message = "O nome é obrigatório") String name,

        @NotNull(message = "A data de nascimento é obrigatória") LocalDate birthDate) {

}
