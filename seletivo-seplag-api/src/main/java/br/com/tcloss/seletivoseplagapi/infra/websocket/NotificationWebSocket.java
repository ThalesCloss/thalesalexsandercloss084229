package br.com.tcloss.seletivoseplagapi.infra.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ApplicationScoped
@ServerEndpoint("/notifications")
public class NotificationWebSocket {
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public NotificationWebSocket(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getId(), session);
        Log.infof("Conexão websocket aberta: %s", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getId());
        Log.infof("Conexão websocket fechada: %s", session.getId());
    }

    public void broadcastMessage(WebSocketMessage message) {
        sessions.values().forEach(session -> {
            try {
                session.getAsyncRemote().sendText(objectMapper.writeValueAsString(message), result -> {
                    if (result.getException() != null) {
                        Log.errorf(result.getException(), "Erro ao enviar notificação websocket: %s", session.getId());
                    }
                });
            } catch (JsonProcessingException e) {
                Log.error("Erro na serialização da mensagem", e);
            }
        });
    }
}
