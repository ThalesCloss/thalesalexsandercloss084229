package br.com.tcloss.seletivoseplagapi.application.events.integration;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlbumCreatedIntegrationEvent(
        UUID id,
        UUID albumId,
        String title,
        String eventName,
        LocalDateTime occurredOn) implements IntegrationEvent {

            public static AlbumCreatedIntegrationEvent create(UUID albumId, String title) {
                return new AlbumCreatedIntegrationEvent(UUID.randomUUID(), albumId, title, "ALBUM_CREATED", LocalDateTime.now());
            }
}
