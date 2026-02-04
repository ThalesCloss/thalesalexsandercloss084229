package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;

public record ArtistProfileCompleteResponseDto(
        UUID id,
        String stageName,
        String biography,
        ArtistType artistType,
        List<LineupResponseDto> lineups) {

    
    public record LineupResponseDto(
            UUID id,
            String label,
            LocalDate startDate,
            LocalDate endDate,
            List<MemberResponseDto> members) {
    }

    public record MemberResponseDto(
            UUID personId,
            boolean isMainArtist,
            boolean isFounder) {
    }
}