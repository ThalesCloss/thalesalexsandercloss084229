package br.com.tcloss.seletivoseplagapi.application.ports;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchArtistProfilesQuery;

public interface ArtistProfileQueryService {

    public MultipleItemsResult<ArtistProfileResponse> searchArtistProfiles(
            SearchArtistProfilesQuery searchArtistProfilesQuery);
}
