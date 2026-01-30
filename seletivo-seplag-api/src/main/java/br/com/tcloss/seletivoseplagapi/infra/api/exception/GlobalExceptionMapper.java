package br.com.tcloss.seletivoseplagapi.infra.api.exception;

import java.time.LocalDateTime;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import io.opentelemetry.api.trace.Span;
import io.quarkus.logging.Log;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        Log.infof("Erro interno %s %s", Span.current().getSpanContext().getTraceId(), exception.getMessage());
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ApiError(
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "Erro Interno",
                        "Ocorreu um erro inesperado. Contate o suporte.",
                        LocalDateTime.now(),
                        null))
                .build();
    }
}