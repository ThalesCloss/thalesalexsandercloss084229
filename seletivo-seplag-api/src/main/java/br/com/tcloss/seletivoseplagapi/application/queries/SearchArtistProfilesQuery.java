package br.com.tcloss.seletivoseplagapi.application.queries;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile.ArtistProfileSearchDto;

public record SearchArtistProfilesQuery(
    ArtistProfileSearchDto artistProfileSearch,
    PaginationInputDto paginationInputDto,
    OrderInputDto orderInputDto
) {

}
