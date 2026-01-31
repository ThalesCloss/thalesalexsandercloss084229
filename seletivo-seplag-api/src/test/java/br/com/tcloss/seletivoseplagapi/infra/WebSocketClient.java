package br.com.tcloss.seletivoseplagapi.infra;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingDeque;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.tcloss.seletivoseplagapi.infra.websocket.WebSocketMessage;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriBuilder;

@Dependent
public class WebSocketClient implements AutoCloseable {
    @Inject
    private ObjectMapper objectMapper;
    private final LinkedBlockingDeque<WebSocketMessage> receivedMessages = new LinkedBlockingDeque<>();
    private WebSocket webSocket;

    private final Listener listener = new Listener() {
        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            try {
                receivedMessages.offer(objectMapper.readValue(data.toString(), WebSocketMessage.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return WebSocket.Listener.super.onText(webSocket, data, last);
        };
    };

    public void connectWebSocket(URI websocketUri) {
        URI wsUri = UriBuilder.fromUri(websocketUri).scheme(websocketUri.getScheme().equals("https")? "wss":"ws").build();
        this.webSocket = HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(wsUri, listener).join();
    }

    public WebSocketMessage getLastMessage() {
        return receivedMessages.peekLast();
    }

    public void clear() {
        receivedMessages.clear();
    }

    @Override
    public void close() throws Exception {
        if (this.webSocket != null) {
            this.webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "End of Test");
        }
    }
}
