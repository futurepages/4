package modules.global.model.entities;

import modules.admin.model.entities.User;
import modules.global.model.entities.core.AbstractPessoaFisica;
import modules.global.model.entities.enums.TipoContatoEnum;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Daiane Algarves 
 */
@Entity
public class PessoaFisica extends AbstractPessoaFisica implements Serializable {

	@Column(unique = true, nullable=false)
	private String cpf;

	@OneToOne
	@Cascade(CascadeType.DETACH)
	private User user;

	@OneToMany
	@Cascade(CascadeType.DETACH)
	private List<Contato> contatos;

	@OneToMany(fetch=FetchType.LAZY)
	 @Cascade(CascadeType.DETACH)
	private List<ContaBancaria> contasBancarias;

	public PessoaFisica() {}

	public PessoaFisica(String cpf, User user) {
		this.cpf = cpf;
		this.user = user;
	}

	@Override
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	@Override
	public List<Contato> getContatos() {
		return contatos;
	}

	public Contato getTelefoneFixoResidencial() {
		Contato telefoneFixo = null;
		for(Contato c : contatos){
			if(c.isPessoal() && c.getTipo().equals(TipoContatoEnum.TELEFONEFIXO)){
				telefoneFixo = c;
				break;
			}
		}
		return telefoneFixo;
	}
	
	public Contato getTelefoneMovelPessoal() {
		Contato telefoneMovel = null;
		for(Contato c : contatos){
			if(c.isPessoal() && c.getTipo().equals(TipoContatoEnum.TELEFONEMOVEL)){
				telefoneMovel = c;
				break;
			}
		}
		return telefoneMovel;
	}
	
	public Contato getEmailPessoal() {
		Contato email = null;
		for(Contato c : contatos){
			if(c.isPessoal() && c.getTipo().equals(TipoContatoEnum.EMAIL)){
				email = c;
				break;
			}
		}
		return email;
	}
	
	@Override
	public List<ContaBancaria> getContasBancarias() {
		return contasBancarias;
	}

	public void setContasBancarias(List<ContaBancaria> contasBancarias) {
		this.contasBancarias = contasBancarias;
	}

	public boolean possuiEsteEmail(String email) {
		if (this.getUser().getEmail().equals(email)) {
			return true;
		}
		List<Contato> contatos = this.getContatos();
		for (Contato contato : contatos) {
			if (contato.getTipo().equals(TipoContatoEnum.EMAIL) && contato.getValor()!=null && contato.getValor().equals(email)) {
				return true;
			}
		}
		return false;
	}
}