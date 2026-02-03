package br.com.tcloss.seletivoseplagapi.application.dtos.input;

import java.time.LocalDate;

import jakarta.ws.rs.QueryParam;

public record PersonSearchDto(
        @QueryParam("name") String name,
        @QueryParam("start_birth_date") LocalDate startBirthDate,
        @QueryParam("end_birth_date") LocalDate endBirthDate) {
}
