package br.com.tcloss.seletivoseplagapi.application.dtos.input.artistprofile;

import jakarta.ws.rs.QueryParam;

public record ArtistProfileSearchDto(
        @QueryParam("stage_name") String stageName,
        @QueryParam("member_name") String memberName

) {

}
