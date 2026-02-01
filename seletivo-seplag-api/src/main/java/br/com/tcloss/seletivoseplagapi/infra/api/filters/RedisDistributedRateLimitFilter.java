package br.com.tcloss.seletivoseplagapi.infra.api.filters;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;

import br.com.tcloss.seletivoseplagapi.infra.api.annotation.RateLimit;
import br.com.tcloss.seletivoseplagapi.infra.api.exception.ApiError;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.RedisKeyNotFoundException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@RateLimit
@Priority(Priorities.USER)
public class RedisDistributedRateLimitFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private final static String KEY_TEMPLATE = "ratelimit:user:%s";
    private final JsonWebToken jsonWebToken;
    private final RedisDataSource redisDataSource;

    private final Integer limit;
    private final Duration window;

    public RedisDistributedRateLimitFilter(JsonWebToken jsonWebToken, RedisDataSource redisDataSource,
            @ConfigProperty(name = "application.rate-limit.limit") Integer limit,
            @ConfigProperty(name = "application.rate-limit.window") Duration window) {
        this.jsonWebToken = jsonWebToken;
        this.redisDataSource = redisDataSource;
        this.limit = limit;
        this.window = window;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final var valueCommands = redisDataSource.value(Integer.class);

        final var key = generateRateLimitUserKey();
        final var currentCount = valueCommands.incr(key);

        selfHealingExpiration(key);

        final var reaming = Math.max(0, limit - currentCount);
        requestContext.getHeaders().add("X-RateLimit-Remaining", Long.toString(reaming));
        
        if (reaming == 0) {
            requestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity(
                            new ApiError(
                                    Response.Status.TOO_MANY_REQUESTS.getStatusCode(),
                                    "Limite de requisições atingido",
                                    null,
                                    LocalDateTime.now(), null))
                    .header("Retry-After", window.toSeconds())
                    .build());
            return;
        }

    }

    private String generateRateLimitUserKey() {
        final var userId = jsonWebToken.getSubject();
        return String.format(KEY_TEMPLATE, userId);
    }

    private void selfHealingExpiration(String key) {
        final var keyCommands = redisDataSource.key();
        try {
            final var keyTTL = keyCommands.ttl(key);
            if (keyTTL == -1) {
                keyCommands.expire(key, window);
            }
        } catch (RedisKeyNotFoundException exception) {
            Log.debugf("Key %s does not exist. Self-healing skipped.", key);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("X-RateLimit-Remaining",
                requestContext.getHeaderString("X-RateLimit-Remaining"));
        responseContext.getHeaders().add("X-RateLimit-Limit", limit);
    }

}
