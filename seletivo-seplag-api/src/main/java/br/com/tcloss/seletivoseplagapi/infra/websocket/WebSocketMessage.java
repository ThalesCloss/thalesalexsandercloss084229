package br.com.tcloss.seletivoseplagapi.infra.websocket;

import java.util.Map;

public record WebSocketMessage(
        String messageType,
        Map<String, String> payload) {

}
