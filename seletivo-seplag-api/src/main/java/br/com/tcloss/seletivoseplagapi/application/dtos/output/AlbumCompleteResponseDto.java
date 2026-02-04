package br.com.tcloss.seletivoseplagapi.application.dtos.output;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public record AlbumCompleteResponseDto(
        String title,
        UUID artistProfileId,
        UUID artistProfileLineupId,
        LocalDate releaseDate,
        List<AlbumTrackResponseDto> tracks,
        List<ImageResponse> images

) {
    public record AlbumTrackGuestResponseDto(
            UUID id,
            String creditName,
            Integer displayOrder) {

    }

    public record AlbumTrackResponseDto(
            String title,
            UUID compositionId,
            Integer discNumber,
            Integer trackNumber,
            Duration contexDuration,
            String isrc,
            List<AlbumTrackGuestResponseDto> guests

    ) {
    }
}
