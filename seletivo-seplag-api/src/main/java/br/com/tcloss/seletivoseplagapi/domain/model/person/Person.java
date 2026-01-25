package br.com.tcloss.seletivoseplagapi.domain.model.person;

import java.time.LocalDate;
import java.util.UUID;

import br.com.tcloss.seletivoseplagapi.domain.shared.AggregateRoot;
import br.com.tcloss.seletivoseplagapi.domain.shared.validation.Notification;
import br.com.tcloss.seletivoseplagapi.domain.shared.vo.ProperName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Person extends AggregateRoot<UUID>{

    private UUID id;

    private ProperName name;

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
        return new Person(UUID.randomUUID(), properName, birthDate);
    }
}
