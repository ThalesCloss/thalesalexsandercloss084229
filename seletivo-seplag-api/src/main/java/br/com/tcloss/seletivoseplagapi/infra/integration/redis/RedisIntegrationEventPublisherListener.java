package br.com.tcloss.seletivoseplagapi.infra.integration.redis;

import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.tcloss.seletivoseplagapi.application.events.integration.AlbumCreatedIntegrationEvent;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.pubsub.ReactivePubSubCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;

@ApplicationScoped
public class RedisIntegrationEventPublisherListener {
    private final ReactivePubSubCommands<RedisIntegrationEvent> pubSub;

    private final String channelName;

    public RedisIntegrationEventPublisherListener(ReactiveRedisDataSource redisDataSource,
            @ConfigProperty(name = "application.redis.events-channel") String channelName) {
        this.pubSub = redisDataSource.pubsub(RedisIntegrationEvent.class);
        this.channelName = channelName;
    }

    public void onAlbumCreated(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) AlbumCreatedIntegrationEvent albumCreatedIntegrationEvent) {

        this.pubSub.publish(channelName, new RedisIntegrationEvent(
                albumCreatedIntegrationEvent.albumId(),
                albumCreatedIntegrationEvent.eventName(),
                albumCreatedIntegrationEvent.occurredOn(),
                Map.of(
                        "albumId", albumCreatedIntegrationEvent.albumId().toString(),
                        "title", albumCreatedIntegrationEvent.title())))
                .subscribe().with(success -> Log.debugf("Evento publicado no canal %s", channelName),
                        error -> Log.errorf(error, "Falha ao publicar evento no canal %s", channelName));
    }
}
