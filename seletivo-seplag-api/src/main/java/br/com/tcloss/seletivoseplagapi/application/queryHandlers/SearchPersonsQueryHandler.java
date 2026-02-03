package br.com.tcloss.seletivoseplagapi.application.queryHandlers;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.PersonResponse;
import br.com.tcloss.seletivoseplagapi.application.ports.PersonQueryService;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchPersonsQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class SearchPersonsQueryHandler {
    private final PersonQueryService personQueryService;

    public MultipleItemsResult<PersonResponse> query(SearchPersonsQuery searchPersonsQuery) {
        return personQueryService.searchPersons(searchPersonsQuery);
    }
}
