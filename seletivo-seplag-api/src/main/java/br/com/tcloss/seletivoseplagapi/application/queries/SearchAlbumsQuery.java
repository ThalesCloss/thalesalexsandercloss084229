package br.com.tcloss.seletivoseplagapi.application.queries;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumSearchDto;

public record SearchAlbumsQuery(
    AlbumSearchDto search,
    PaginationInputDto pagination,
    OrderInputDto orderInputDto
) {

}
