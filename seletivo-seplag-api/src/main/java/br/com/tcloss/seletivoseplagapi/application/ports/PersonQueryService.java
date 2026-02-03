package br.com.tcloss.seletivoseplagapi.application.ports;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.PersonResponse;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchPersonsQuery;

public interface PersonQueryService {
    public MultipleItemsResult<PersonResponse> searchPersons(SearchPersonsQuery searchPersonsQuery);
}
