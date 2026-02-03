package br.com.tcloss.seletivoseplagapi.application.ports;

import java.time.LocalDate;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.OrderInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;

public interface AlbumQueryService {
    public MultipleItemsResult<AlbumSummaryDataProjection> searchAlbums(AlbumSearchDto filter, PaginationInputDto pagination, OrderInputDto orderInputDto);

    record AlbumSummaryDataProjection(
            UUID albumId,
            String title,
            UUID artistId,
            String artistName,
            LocalDate releaseDate,
            Integer tracks,
            ImageDataProjection image) {
    }
    record ImageDataProjection(
        String imageUrl,
        String type
    ) {}
}
