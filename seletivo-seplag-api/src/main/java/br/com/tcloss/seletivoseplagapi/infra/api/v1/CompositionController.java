package br.com.tcloss.seletivoseplagapi.infra.api.v1;


import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateCompositionCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateCompositionCommand;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.CompositionRepository;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("/v1/compositions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class CompositionController {
    private final CreateCompositionCommandHandler createCompositionCommandHandler;
    private final CompositionRepository compositionRepository;
    @POST
    public Response create(CreateCompositionCommand commandRequest) {
        createCompositionCommandHandler.execute(commandRequest);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        final var compositions = compositionRepository.getById(id).orElseThrow();
        return Response.ok(compositions).build();
    }
}
