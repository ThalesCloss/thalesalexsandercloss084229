package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.time.LocalDate;
import java.util.UUID;

public record PersonResponse(
    UUID personId,
    String name,
    LocalDate birDate
) {
    

}
