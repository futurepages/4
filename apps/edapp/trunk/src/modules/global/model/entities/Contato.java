package modules.global.model.entities;

import modules.global.model.entities.enums.TipoContatoEnum;
import org.apache.commons.lang.NotImplementedException;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Daiane Algarves
 */
@Entity
public class Contato implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String valor;

	@Enumerated(EnumType.STRING)
	private TipoContatoEnum tipo;

	private boolean pessoal;

	private String valorComplemento;

	private String obs;

	public Contato() {

	}

	public Contato(String valor, TipoContatoEnum tipo, String valorComplemento, boolean pessoal, String obs) {
		this.valor = valor;
		this.tipo = tipo;
		this.valorComplemento = valorComplemento;
		this.pessoal = pessoal;
		this.obs = obs;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public boolean isPessoal() {
		return pessoal;
	}

	public void setPessoal(boolean pessoal) {
		this.pessoal = pessoal;
	}

	public TipoContatoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoContatoEnum tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		if (valor != null) {
			valor = valor.toLowerCase();
		}
		this.valor = valor;
	}

	public String getValorComplemento() {
		return valorComplemento;
	}

	public void setValorComplemento(String valorComplemento) {
		this.valorComplemento = valorComplemento;
	}

	/**
	 * ATENÇÃO!!! Só funciona para email por enquanto.
	 */
	public String getPrefixo() {
		if (this.getTipo() == TipoContatoEnum.EMAIL) {
			String[] mailParts = this.getValor().split("@");
			String emailPrefix = mailParts[0];
			return emailPrefix;
		} else {
			//implementar futuramente para felefone
			throw new NotImplementedException();
		}
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.valor != null ? this.valor.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Contato other = (Contato) obj;
		if ((this.valor == null) ? (other.valor != null) : !this.valor.equals(other.valor) || !this.tipo.equals(other.tipo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return valor;
	}
}
