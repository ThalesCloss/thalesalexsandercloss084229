package br.com.tcloss.seletivoseplagapi.infra.integration.redis;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record RedisIntegrationEvent(
    UUID id,
    String eventType,      
    LocalDateTime createdAt,
    Map<String, String> payload 
) {
    
}