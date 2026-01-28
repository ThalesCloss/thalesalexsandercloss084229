package br.com.tcloss.seletivoseplagapi.infra.api.exception;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        int status,
        String title,
        String detail,
        LocalDateTime timestamp,
        Map<String, String> errors

) {
}
