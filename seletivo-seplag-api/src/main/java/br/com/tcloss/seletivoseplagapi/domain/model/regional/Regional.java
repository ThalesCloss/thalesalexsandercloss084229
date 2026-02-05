package br.com.tcloss.seletivoseplagapi.domain.model.regional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regionais")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Regional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInterno;

    @Column(insertable = true, updatable = false, name = "id_regional_api")
    private int id;

    @Column(name = "nome", length = 200)
    private String nome;

    @Column(name = "ativo")
    private Boolean ativo;

    public Regional(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.ativo=true;
    }

    public void desativar() {
        ativo = false;
    }

    public Boolean nomeFoiAlterado(String nome){
        return !this.nome.equals(nome);
    }

}
