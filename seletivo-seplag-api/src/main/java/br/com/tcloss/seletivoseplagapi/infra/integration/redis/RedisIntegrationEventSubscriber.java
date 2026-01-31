package br.com.tcloss.seletivoseplagapi.infra.integration.redis;

import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.tcloss.seletivoseplagapi.infra.websocket.NotificationWebSocket;
import br.com.tcloss.seletivoseplagapi.infra.websocket.WebSocketMessage;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class RedisIntegrationEventSubscriber implements Consumer<RedisIntegrationEvent> {
    private final String channelName;

    public RedisIntegrationEventSubscriber(
            @ConfigProperty(name = "application.redis.events-channel") String channelName,
            RedisDataSource redisDataSource,
            NotificationWebSocket webSocket) {
        this.channelName = channelName;
        this.redisDataSource = redisDataSource;
        this.webSocket = webSocket;
    }

    private final RedisDataSource redisDataSource;

    private final NotificationWebSocket webSocket;

    @PostConstruct
    public void init() {
        redisDataSource.pubsub(RedisIntegrationEvent.class).subscribe(channelName, this);

        Log.infof("Redis Pub/Sub iniciado. Ouvindo canal: %s", channelName);
    }

    @Override
    public void accept(RedisIntegrationEvent redisMessage) {
        webSocket.broadcastMessage(new WebSocketMessage(redisMessage.eventType(), redisMessage.payload()));
    }

}
