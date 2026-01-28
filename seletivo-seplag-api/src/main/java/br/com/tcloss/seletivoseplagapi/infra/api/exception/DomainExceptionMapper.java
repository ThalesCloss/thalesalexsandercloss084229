package br.com.tcloss.seletivoseplagapi.infra.api.exception;

import java.time.LocalDateTime;

import br.com.tcloss.seletivoseplagapi.domain.shared.validation.DomainException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

    @Override
    public Response toResponse(DomainException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ApiError(Response.Status.BAD_REQUEST.getStatusCode(), "Violação de regras de negócio",
                        exception.getMessage(), LocalDateTime.now(), null))
                .build();
    }

}
