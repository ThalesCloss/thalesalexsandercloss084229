package br.com.tcloss.seletivoseplagapi.infra.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class JacksonExceptionMapper implements ExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException exception) {
        String fieldName = exception.getPath().isEmpty()
                ? "unknown"
                : exception.getPath().get(exception.getPath().size() - 1).getFieldName();

        var error = new ApiError(
                400,
                "Erro de Formatação JSON",
                "O campo '" + fieldName + "' possui um valor inválido.", LocalDateTime.now(), null);

        return Response.status(400).entity(error).build();
    }
}