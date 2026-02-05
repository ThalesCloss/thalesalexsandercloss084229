package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.SincronizarRegionaisCommandHandler;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Authenticated
@AllArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/v1/api-regionais")
public class RegionaisController {
    private final SincronizarRegionaisCommandHandler sincronizarRegionaisCommandHandler;

    @POST
    @RolesAllowed("api-regionais")
    @Path("/sincronizar")
    public Response sincronizar() {
        final var resultado = sincronizarRegionaisCommandHandler.execute();
        return Response.ok(resultado).build();
    }

}
