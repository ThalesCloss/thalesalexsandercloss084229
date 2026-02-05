package br.com.tcloss.seletivoseplagapi.infra.api.v1;

import br.com.tcloss.seletivoseplagapi.application.commandHandlers.CreatePersonCommandHandler;
import br.com.tcloss.seletivoseplagapi.application.commands.CreatePersonCommand;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PersonSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.PersonResponse;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchPersonsQuery;
import br.com.tcloss.seletivoseplagapi.application.queryHandlers.SearchPersonsQueryHandler;
import br.com.tcloss.seletivoseplagapi.infra.api.annotation.RateLimit;
import io.quarkus.security.Authenticated;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.AllArgsConstructor;

@Path("/v1/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
@Authenticated
@RateLimit
public class PersonController {
    private final CreatePersonCommandHandler createPersonCommandHandler;
    private final SearchPersonsQueryHandler searchPersonsQueryHandler;

    @POST
    public Response create(@Valid CreatePersonCommand commandRequest, @Context UriInfo uriInfo) {
        final var person = createPersonCommandHandler.execute(commandRequest);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(person.getId().toString()).build())
                .entity(person)
                .build();
    }

    @GET
    public MultipleItemsResult<PersonResponse> search(
            @BeanParam PersonSearchDto personSearchDto,
            @BeanParam PaginationInputDto paginationInputDto,
            @BeanParam OrderInputDto orderInputDto) {
        return searchPersonsQueryHandler.query(new SearchPersonsQuery(
                personSearchDto, paginationInputDto, orderInputDto));
    }

}
