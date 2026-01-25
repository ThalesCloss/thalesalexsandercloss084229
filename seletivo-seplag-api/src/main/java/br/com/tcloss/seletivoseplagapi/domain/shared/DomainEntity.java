package br.com.tcloss.seletivoseplagapi.domain.shared;

import java.io.Serializable;

public interface DomainEntity<ID> extends Serializable {
    ID getId();
}
