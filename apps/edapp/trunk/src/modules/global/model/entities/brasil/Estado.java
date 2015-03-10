package modules.global.model.entities.brasil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma Unidade Federativa do Brasil (Estado).
 */
@Entity
public class Estado implements Serializable {

    @Id
    private String sigla;
    
    @Column(length=150)
    private String nome;

    @OneToOne
    private Cidade capital;

    @ManyToOne
    private Regiao regiao;


    @ManyToMany
    private List<Estado> estadosVizinhos;

    public Estado(){}
    
    @Override
    public String toString() {
        return this.nome + " ("+this.sigla+")";
    }
    
    public Estado(String estadoSigla) {
        this.sigla = estadoSigla;
    }

    public Estado(String estadoSigla, String estadoNome, String regiaoSigla) {
        this.sigla = estadoSigla;
        this.nome = estadoNome;
        this.regiao = new Regiao(regiaoSigla);
    }

    public Estado(String estadoSigla, String estadoNome, String regiaoSigla, String[] vizinhos) {
        this.sigla = estadoSigla;
        this.nome = estadoNome;
        this.regiao = new Regiao(regiaoSigla);
        List<Estado> lista = new ArrayList<Estado>();
        for (String s : vizinhos) {
            lista.add(new Estado(s));
        }
        this.estadosVizinhos = lista;
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

    public Cidade getCapital() {
        return capital;
    }

    public void setCapital(Cidade capital) {
        this.capital = capital;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }

    public List<Estado> getEstadosVizinhos() {
        return estadosVizinhos;
    }

    public void setEstadosVizinhos(List<Estado> estadosVizinhos) {
        this.estadosVizinhos = estadosVizinhos;
    }
}