package br.com.tcloss.seletivoseplagapi.application.queryHandlers;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.ports.ArtistProfileQueryService;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchArtistProfilesQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class SearchArtistProfilesQueryHandler {

    private final ArtistProfileQueryService artistProfileQueryService;

    public MultipleItemsResult<ArtistProfileResponse> query(SearchArtistProfilesQuery searchArtistProfilesQuery) {
        return artistProfileQueryService.searchArtistProfiles(searchArtistProfilesQuery);
    }
}
