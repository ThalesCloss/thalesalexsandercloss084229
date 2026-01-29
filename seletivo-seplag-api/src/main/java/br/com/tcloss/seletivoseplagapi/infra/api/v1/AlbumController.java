package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/V1/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlbumController {

    @POST
    public Response criar(@Valid AlbumDto album) {
        return Response.ok().build();
        
    }
}
