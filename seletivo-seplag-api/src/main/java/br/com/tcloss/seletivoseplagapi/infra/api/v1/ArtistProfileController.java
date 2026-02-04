package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.ChangeLineupCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateArtistProfileCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commandHandlers.UpdateArtistProfileCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.ChangeLineupCommand;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.application.commands.UpdateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.LineupDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.UpdateArtistProfileDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.queries.ArtistProfileQuery;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchArtistProfilesQuery;
import br.com.tcloss.seletivoseplagapi.application.queryHandlers.ArtistProfileQueryHandler;
import br.com.tcloss.seletivoseplagapi.application.queryHandlers.SearchArtistProfilesQueryHandler;
import br.com.tcloss.seletivoseplagapi.infra.api.annotation.RateLimit;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
    final private SearchArtistProfilesQueryHandler searchArtistProfilesQueryHandler;
    final private ArtistProfileQueryHandler artistProfileQueryHandler;
    final private ChangeLineupCommandHandler changeLineupCommandHandler;
    final private UpdateArtistProfileCommandHandler updateArtistProfileCommandHandler;

    @POST
    public Response create(ArtistProfileDto request) {
        createArtistProfileCommandHandler.execute(new CreateArtistProfileCommand(request));
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        final var artist = artistProfileQueryHandler.query(new ArtistProfileQuery(id));
        if (artist == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        }
        return Response.ok(artist).build();
    }

    @POST
    @Path("/{id}/lineups")
    public Response changeLineup(@PathParam("id") UUID id, LineupDto lineupDto) {
        changeLineupCommandHandler.execute(new ChangeLineupCommand(id, lineupDto));
        return Response.accepted().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") UUID id, UpdateArtistProfileDto updateArtistProfileDto) {
        updateArtistProfileCommandHandler.execute(new UpdateArtistProfileCommand(id, updateArtistProfileDto));
        return Response.accepted().build();
    }

    @GET
    public MultipleItemsResult<ArtistProfileResponse> search(@BeanParam ArtistProfileSearchDto artistProfileSearchDto,
            @BeanParam PaginationInputDto paginationInputDto, @BeanParam OrderInputDto orderInputDto) {
        return searchArtistProfilesQueryHandler
                .query(new SearchArtistProfilesQuery(artistProfileSearchDto, paginationInputDto, orderInputDto));
    }
}
