package br.com.tcloss.seletivoseplagapi.domain.model.person;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
public class PersonTest {

    @Test
    @DisplayName("Deve criar uma pessoa com sucesso")
    void shouldCreatePersonSuccessfully() {
        final var name = "Ozzy Osbourne";
        final var birthDate = LocalDate.of(1948, 12, 3);    
        final var person = Person.createNew(name, birthDate);
        assertThat(person).isNotNull();
        assertThat(person.getId()).isNotNull();
        assertThat(person.getName().toString()).isEqualTo(name);
        assertThat(person.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    @DisplayName("Deve lançar erro ao criar pessoa com nome inválido")
    void shouldThrowErrorForInvalidName() {
        assertThatThrownBy(() -> {
            Person.createNew("O", LocalDate.of(1948, 12, 3));
        }).hasMessageContaining("Nome deve ter pelo menos 2 caracteres");
    }

    @Test
    @DisplayName("Deve lançar erro ao criar pessoa com data de nascimento futura")
    void shouldThrowErrorForFutureBirthDate() {
        assertThatThrownBy(() -> {  
            Person.createNew("Ozzy Osbourne", LocalDate.now().plusDays(1));
        }).hasMessageContaining("A data de nascimento não pode ser futura");
    }

    @Test
    @DisplayName("Deve lançar erro ao criar pessoa com data de nascimento nula")
    void shouldThrowErrorForNullBirthDate() {
        assertThatThrownBy(() -> {  
            Person.createNew("Ozzy Osbourne", null);
        }).hasMessageContaining("A data de nascimento é obrigatória");  
    }

    @Test
    @DisplayName("Deve lançar erro ao criar pessoa com nome inválido e data de nascimento vazia")
    void shouldThrowErrorForInvalidNameAndNullBirthDate() {
        assertThatThrownBy(() -> {
            Person.createNew("O", null);
        }).hasMessageContaining("Nome deve ter pelo menos 2 caracteres").hasMessageContaining("A data de nascimento é obrigatória");
    }
}
