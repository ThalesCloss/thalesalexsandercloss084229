package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateAlbumCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreateAlbumImageCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateAlbumCommand;
import br.com.tcloss.seletivoseplagapi.application.commands.CreateAlbumImageCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.ImageDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.AlbumResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.queries.AlbumQuery;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchAlbumsQuery;
import br.com.tcloss.seletivoseplagapi.application.queryHandlers.AlbumQueryHandler;
import br.com.tcloss.seletivoseplagapi.application.queryHandlers.SearchAlbumsQueryHandler;
import br.com.tcloss.seletivoseplagapi.infra.api.annotation.RateLimit;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("/v1/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Authenticated
@RateLimit
public class AlbumController {
    private final CreateAlbumCommandHandler createAlbumCommandHandler;
    private final CreateAlbumImageCommandHandler createAlbumImageCommandHandler;
    private final SearchAlbumsQueryHandler searchAlbumsQueryHandler;
    final private AlbumQueryHandler albumQueryHandler;

    @POST
    public Response criar(@Valid AlbumDto album) {
        createAlbumCommandHandler.execute(new CreateAlbumCommand(album));
        return Response.ok().build();

    }

    @POST
    @Path("/{albumId}/images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(@PathParam("albumId") UUID albumId, @Valid ImageDto image) {
        createAlbumImageCommandHandler.execute(new CreateAlbumImageCommand(albumId, image));
        return Response.ok().build();
    }

    @GET
    public MultipleItemsResult<AlbumResponse> search(@BeanParam AlbumSearchDto albumSearchDto,
            @BeanParam PaginationInputDto pagination, @BeanParam OrderInputDto orderInputDto) {
        return searchAlbumsQueryHandler.query(new SearchAlbumsQuery(albumSearchDto, pagination, orderInputDto));
    }

     @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        final var album = albumQueryHandler.query(new AlbumQuery(id));
        if (album == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        }
        return Response.ok(album).build();
    }


}
