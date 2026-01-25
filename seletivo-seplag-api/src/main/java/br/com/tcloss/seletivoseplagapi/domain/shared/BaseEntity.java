package br.com.tcloss.seletivoseplagapi.domain.shared;

import java.util.Objects;

public abstract class BaseEntity<ID> implements DomainEntity<ID> {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof DomainEntity)) {
            return false;
        }
        DomainEntity<?> that = (DomainEntity<?>) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
