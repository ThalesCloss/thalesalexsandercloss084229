package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.util.List;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistType;

public record ArtistProfileResponse(
    UUID artistProfileId,
    String stageName,
    String biography,
    ArtistType artistType,
    int lineups,
    List<ArtistProfileMember> lastLineup
) {

    public record ArtistProfileMember(
        UUID personId,
        String personName,
        Boolean isMainArtist,
        Boolean isFounder
    ){}
}
