package modules.global.model.entities.brasil;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Representa uma Unidade Federativa do Brasil (Estado).
 */
@Entity
public class Regiao implements Serializable {

    @Id
    private String sigla;

    @Column(length = 150)
    private String nome;

    @OneToMany(mappedBy = "regiao", cascade = CascadeType.ALL)
    private List<Estado> estadoList;

    public Regiao() {
    }

    public Regiao(String sigla) {
        this.sigla = sigla;
    }

    public Regiao(String sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }


    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Estado> getEstadoList() {
        return estadoList;
    }

    public void setEstadoList(List<Estado> estadoList) {
        this.estadoList = estadoList;
    }

}
