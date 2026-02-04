package br.com.tcloss.seletivoseplagapi.application.queryHandlers;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileCompleteResponseDto;
import br.com.tcloss.seletivoseplagapi.application.mappers.ArtistProfileCompleteMapper;
import br.com.tcloss.seletivoseplagapi.application.queries.ArtistProfileQuery;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfileRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class ArtistProfileQueryHandler {

    private final ArtistProfileRepository artistProfileRepository;
    private final ArtistProfileCompleteMapper artistProfileCompleteMapper;

    public ArtistProfileCompleteResponseDto query(ArtistProfileQuery artistProfileQuery) {
        final var artist = artistProfileRepository.getById(artistProfileQuery.id());
        if (artist.isEmpty()) {
            return null;
        }
        return artistProfileCompleteMapper.toResponse(artist.get());
    }
}
