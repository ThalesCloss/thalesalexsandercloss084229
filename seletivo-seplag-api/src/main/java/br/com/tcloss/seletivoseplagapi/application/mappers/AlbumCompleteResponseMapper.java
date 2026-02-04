package br.com.tcloss.seletivoseplagapi.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.AlbumCompleteResponseDto;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Album;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Guest;
import br.com.tcloss.seletivoseplagapi.domain.model.album.ISRC;
import br.com.tcloss.seletivoseplagapi.domain.model.album.Track;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import org.mapstruct.Named;

@Mapper(componentModel = "jakarta", uses = { ImageMapper.class })
public interface AlbumCompleteResponseMapper {

    @Mapping(target = "title", source = "title", qualifiedByName = "mapProperName")
    AlbumCompleteResponseDto toResponse(Album album);

    @Mapping(target = "title", source = "title", qualifiedByName = "mapProperName")
    @Mapping(target = "isrc", source = "isrc", qualifiedByName = "mapIsrc")
    @Mapping(target = "guests", source = "guests")
    AlbumCompleteResponseDto.AlbumTrackResponseDto toTrackResponse(Track track);

    @Mapping(target = "id", source = "personId")
    AlbumCompleteResponseDto.AlbumTrackGuestResponseDto toGuestResponse(Guest guest);

    @Named("mapProperName")
    default String mapProperName(ProperName properName) {
        return properName != null ? properName.toString() : null;
    }

    @Named("mapIsrc")
    default String mapIsrc(ISRC isrc) {
        return isrc != null ? isrc.isrc() : null;
    }
}
