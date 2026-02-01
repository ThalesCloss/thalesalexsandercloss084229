package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateAlbumCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateAlbumCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumDto;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("/v1/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Authenticated
public class AlbumController {
    private final CreateAlbumCommandHandler createAlbumCommandHandler;

    @POST
    public Response criar(@Valid AlbumDto album) {
        createAlbumCommandHandler.execute(new CreateAlbumCommand(album));
        return Response.ok().build();

    }
}
