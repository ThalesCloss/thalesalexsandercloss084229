package br.com.tcloss.seletivoseplagapi.application.dtos.input.album;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.ws.rs.QueryParam;

public record AlbumSearchDto(
        @QueryParam("start_release_date") LocalDate startReleaseDate,
        @QueryParam("end_release_date") LocalDate endReleaseDate,
        @QueryParam("title") String albumTitle,
        @QueryParam("artist") String artistName,
        @QueryParam("specific_artists") List<UUID> specificArtists,
        @QueryParam("search_in_members") @Schema(description = "Realiza a busca pelo nome do artista nos membros de uma banda por exemplo") boolean searchInMembers,

        @QueryParam("search_in_guests") @Schema(description = "Realiza a busca pelo nome do artista nos convidados de um álbum") boolean searchInGuests) {
    public boolean searchSpecificArtists() {
        return specificArtists != null && !specificArtists.isEmpty();
    }
}
