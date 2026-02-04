package br.com.tcloss.seletivoseplagapi.application.queryHandlers;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.AlbumCompleteResponseDto;
import br.com.tcloss.seletivoseplagapi.application.mappers.AlbumCompleteResponseMapper;
import br.com.tcloss.seletivoseplagapi.application.queries.AlbumQuery;
import br.com.tcloss.seletivoseplagapi.domain.model.album.AlbumRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class AlbumQueryHandler {

    private final AlbumRepository albumRepository;
    private final AlbumCompleteResponseMapper albumCompleteResponseMapper;

    public AlbumCompleteResponseDto query(AlbumQuery albumQuery) {
        final var album = albumRepository.getById(albumQuery.id());
        if (album.isEmpty()) {
            return null;
        }
        return albumCompleteResponseMapper.toResponse(albumRepository.getById(albumQuery.id()).get());
    }
}
