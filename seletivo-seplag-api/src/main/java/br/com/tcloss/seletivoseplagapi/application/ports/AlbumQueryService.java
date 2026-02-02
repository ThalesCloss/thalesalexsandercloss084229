package br.com.tcloss.seletivoseplagapi.application.ports;

import java.time.LocalDate;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumSearchDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.PaginationInputDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.output.MultipleItemsResult;

public interface AlbumQueryService {
    public MultipleItemsResult<AlbumSummaryDataProjection> searchAlbums(AlbumSearchDto filter, PaginationInputDto pagination);

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
