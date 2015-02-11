package modules.global.model.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Daiane Algarves
 */
@Entity
public class Predio implements Serializable {

    @Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
	
    private String nome;

    @OneToOne
    private Endereco endereco;

	@Column(unique=true)
    private String nomeBusca;
    
    public Predio() {

	}

	public Predio(String nome, Endereco endereco) {
		this.nome = nome;
		this.endereco = endereco;
	}

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeBusca() {
		nomeBusca = toString();
        return nomeBusca;
    }

    public void setNomeBusca(String nomeBusca) {
        this.nomeBusca = nomeBusca;
    }

	@Override
	public String toString() {
		return nome+" - "+endereco.getCidade();
	}


	

}