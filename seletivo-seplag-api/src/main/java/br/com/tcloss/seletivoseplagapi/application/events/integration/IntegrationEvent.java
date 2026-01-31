package br.com.tcloss.seletivoseplagapi.application.events.integration;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IntegrationEvent {
  UUID id();
  LocalDateTime occurredOn();
  String eventName();
}
