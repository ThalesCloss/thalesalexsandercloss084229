package br.com.tcloss.seletivoseplagapi.application.queries;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PersonSearchDto;

public record SearchPersonsQuery(
        PersonSearchDto query,
        PaginationInputDto pagination,
        OrderInputDto order) {

}
