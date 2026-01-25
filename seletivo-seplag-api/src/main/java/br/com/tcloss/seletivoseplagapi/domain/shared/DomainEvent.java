package br.com.tcloss.seletivoseplagapi.domain.shared;

import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent  extends Serializable{
    Instant occurredOn();
}
