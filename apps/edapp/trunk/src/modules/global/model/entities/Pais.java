package modules.global.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Pais implements Serializable {

	@Id
	private String sigla;
	@Column(unique=true)
	private String nome;	
	@Column(columnDefinition = "boolean default false")
	private boolean ativo;

	public Pais() {
	}

	public Pais(String sigla) {
		this.sigla = sigla.toUpperCase();
	}

	public Pais(String sigla, String nome){		
		this.sigla = sigla.toUpperCase();
		this.nome = nome;
		this.ativo = true;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla.toUpperCase();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}		

	@Override
	public String toString() {
		return this.nome;
	}
}