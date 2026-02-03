package br.com.tcloss.seletivoseplagapi.application.mappers;

import org.mapstruct.Mapper;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.AlbumResponse;
import br.com.tcloss.seletivoseplagapi.application.ports.AlbumQueryService.AlbumSummaryDataProjection;

@Mapper(componentModel = "jakarta", uses = { ImageMapper.class })
public interface AlbumResponseMapper {

    AlbumResponse toResponse(AlbumSummaryDataProjection albumSummaryDataProjection);
}
