package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateArtistProfileCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileDto;
import br.com.tcloss.seletivoseplagapi.infra.api.annotation.RateLimit;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("/v1/artist-profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Authenticated
@RateLimit
public class ArtistProfileController {
    final private CreateArtistProfileCommandHandler createArtistProfileCommandHandler;

    @POST
    public Response create(ArtistProfileDto request) {
        createArtistProfileCommandHandler.execute(new CreateArtistProfileCommand(request));
        return Response.ok().build();
    }
}
