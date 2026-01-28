package br.com.tcloss.seletivoseplagapi.domain.model.person;

import java.time.LocalDate;
import java.util.UUID;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.domain.shared.AggregateRoot;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "persons")
public class Person extends AggregateRoot<UUID>{

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Embedded
    @AttributeOverride(
        name = "name",
        column = @Column(name = "name", nullable = false, length = 200)
    )
    private ProperName name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    public static Person createNew(String name, LocalDate birthDate) {
        final var notification = Notification.create();
        ProperName properName = null;
        try{
            properName = new ProperName(name);
        } catch (Exception e) {
            notification.append(e.getMessage());
        }
        notification.validate(birthDate != null, "A data de nascimento é obrigatória");
        notification.validate(birthDate != null && !birthDate.isAfter(LocalDate.now()), "A data de nascimento não pode ser futura");
        notification.throwIfHasErrors();
        return new Person(UuidCreator.getTimeOrderedEpoch(), properName, birthDate);
    }
}
