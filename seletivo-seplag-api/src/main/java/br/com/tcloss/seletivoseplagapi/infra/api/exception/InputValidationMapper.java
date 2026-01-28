package br.com.tcloss.seletivoseplagapi.infra.api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InputValidationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String fieldName = this.lastNode(violation.getPropertyPath().toString());
            errors.put(fieldName, violation.getMessage());
        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(
                        new ApiError(
                                Response.Status.BAD_REQUEST.getStatusCode(),
                                "Dados inválidos",
                                null,
                                LocalDateTime.now(),
                                errors))
                .build();

    }

    private String lastNode(String path) {
        int lastDot = path.lastIndexOf('.');
        return (lastDot != -1) ? path.substring(lastDot + 1) : path;
    }

}
