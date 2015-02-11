package modules.global.model.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import modules.global.model.entities.brasil.Cidade;
import org.futurepages.util.Is;

/**
 *
 * @author Daiane Algarves
 */
@Entity
public class Endereco implements Serializable {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String logradouro;

    private String bairro;

    private String cep;

    private String googleMapsUrl;

    @ManyToOne//muitos p/ 1.cidade é chave estrangeira
    private Cidade cidade;
    
	public Endereco() {

	}

	public Endereco(String logradouro, String bairro, String cep, String googleMapsUrl, Cidade cidade) {
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.cep = cep;
		this.googleMapsUrl = googleMapsUrl;
		this.cidade = cidade;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getGoogleMapsUrl() {
		return googleMapsUrl;
	}

	public void setGoogleMapsUrl(String googleMapsUrl) {
		this.googleMapsUrl = googleMapsUrl;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	@Override
	public String toString() {
		if (!Is.empty(logradouro) && !Is.empty(bairro) && cidade !=null ){
			return logradouro + " - " + bairro  + " - " + cidade.getNomeBusca();
		}
		return "";
	}
}