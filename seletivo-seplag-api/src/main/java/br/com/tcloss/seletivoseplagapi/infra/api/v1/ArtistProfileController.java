package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateArtistProfileCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateArtistProfileCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchArtistProfilesQuery;
import br.com.tcloss.seletivoseplagapi.application.queryHandlers.SearchArtistProfilesQueryHandler;
import br.com.tcloss.seletivoseplagapi.infra.api.annotation.RateLimit;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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
    final private SearchArtistProfilesQueryHandler searchArtistProfilesQueryHandler;

    @POST
    public Response create(ArtistProfileDto request) {
        createArtistProfileCommandHandler.execute(new CreateArtistProfileCommand(request));
        return Response.ok().build();
    }

    @GET
    public MultipleItemsResult<ArtistProfileResponse> search(@BeanParam ArtistProfileSearchDto artistProfileSearchDto,
            @BeanParam PaginationInputDto paginationInputDto, @BeanParam OrderInputDto orderInputDto) {
        return searchArtistProfilesQueryHandler
                .query(new SearchArtistProfilesQuery(artistProfileSearchDto, paginationInputDto, orderInputDto));
    }
}
