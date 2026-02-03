package br.com.tcloss.seletivoseplagapi.application.queryHandlers;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.ImageResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.AlbumResponse;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;
import br.com.tcloss.seletivoseplagapi.application.mappers.AlbumResponseMapper;
import br.com.tcloss.seletivoseplagapi.application.ports.AlbumQueryService;
import br.com.tcloss.seletivoseplagapi.application.queries.SearchAlbumsQuery;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class SearchAlbumsQueryHandler {

    private final AlbumQueryService albumQueryService;
    private final AlbumResponseMapper albumResponseMapper;

    public MultipleItemsResult<AlbumResponse> query(SearchAlbumsQuery query) {
        final var albums = albumQueryService.searchAlbums(query.search(), query.pagination(), query.orderInputDto());
        
        final var re = albums.data().stream().map(albumResponseMapper::toResponse).toList();
        return new MultipleItemsResult<>(re, albums.pagination());
    }
}
