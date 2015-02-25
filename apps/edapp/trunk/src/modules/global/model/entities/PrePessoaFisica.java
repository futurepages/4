package modules.global.model.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import modules.global.model.entities.core.AbstractPessoaFisica;
import modules.global.model.entities.enums.TipoContatoEnum;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author Severiano Alves
 */
@Entity
public class PrePessoaFisica extends AbstractPessoaFisica implements Serializable {

	private String cpf;

	@OneToMany(cascade=javax.persistence.CascadeType.ALL)
	private List<Contato> contatos;


	@OneToMany(fetch=FetchType.LAZY)
	@Cascade(CascadeType.DETACH)
	private List<ContaBancaria> contasBancarias;


	public PrePessoaFisica() {

	}

	public PrePessoaFisica(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public List<Contato> getContatos() {
		return contatos;
	}

	public Contato getEmailTrabalho() {
		Contato email = null;
		for(Contato c : contatos){
			if(!c.isPessoal() && c.getTipo().equals(TipoContatoEnum.EMAIL)){
				email = c;
				break;
			}
		}
		return email;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	public void setContasBancarias(List<ContaBancaria> contasBancarias) {
		this.contasBancarias = contasBancarias;
	}

	@Override
	public List<ContaBancaria> getContasBancarias() {
		return contasBancarias;
	}
}