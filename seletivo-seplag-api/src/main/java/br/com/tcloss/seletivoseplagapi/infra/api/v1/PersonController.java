package br.com.tcloss.seletivoseplagapi.infra.api.v1;


import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreatePersonCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreatePersonCommand;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("/V1/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class PersonController {
    private final CreatePersonCommandHandler createPersonCommandHandler;

    @POST
    public Response create(@Valid CreatePersonCommand commandRequest) {
        createPersonCommandHandler.execute(commandRequest);
        return Response.status(Response.Status.CREATED).build();
    }

}
