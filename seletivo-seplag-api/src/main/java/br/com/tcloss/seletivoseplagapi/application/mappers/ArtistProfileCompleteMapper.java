package br.com.tcloss.seletivoseplagapi.application.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import br.com.tcloss.seletivoseplagapi.application.dtos.output.ArtistProfileCompleteResponseDto;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfile;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Lineup;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.Member;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;

@Mapper(componentModel = "jakarta")
public abstract class ArtistProfileCompleteMapper {

    @Mapping(target = "stageName", source = "stageName", qualifiedByName = "mapProperName")
    public abstract ArtistProfileCompleteResponseDto toResponse(ArtistProfile entity);

    @Mapping(target = "label", source = "label", qualifiedByName = "mapProperName")
    @Mapping(target = "startDate", source = "duration.startDate")
    @Mapping(target = "endDate", source = "duration.endDate")
    protected abstract ArtistProfileCompleteResponseDto.LineupResponseDto toLineupDto(Lineup entity);

    protected abstract ArtistProfileCompleteResponseDto.MemberResponseDto toMemberDto(Member entity);


    @Named("mapProperName")
    protected String mapProperName(ProperName properName) {
        if (properName == null) return null;
        return properName.toString();
    }
}