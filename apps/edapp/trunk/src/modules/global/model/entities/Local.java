package modules.global.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import modules.global.model.entities.brasil.Cidade;

@Entity
public class Local implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@Column(length = 120,nullable=false)
	private String nome;

    @Column(length=400)
    private String endereco;

    @ManyToOne
    private Cidade cidade;

    @Lob
    private String googleMapsURL;

	public Local() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString(){
		return this.getNome();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String logradouro) {
		this.endereco = logradouro;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getGoogleMapsURL() {
		return googleMapsURL;
	}

	public void setGoogleMapsURL(String googleMapsURL) {
		this.googleMapsURL = googleMapsURL;
	}
}