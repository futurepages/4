package modules.global.model.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Severiano Alves
 */
@Entity
public class Banco  implements Serializable{

	@Id
	private int codigo;

	private String nome;

	private String rotulo;
	
	private boolean visivel = true;

	public Banco() {
	}

	public Banco(int codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}

	public Banco(int codigo, String nome, String rotulo) {
		this.codigo = codigo;
		this.nome = nome;
		this.rotulo = rotulo;
	}
	
	public Banco(int codigo, String nome, String rotulo, boolean visivel) {
		this.codigo = codigo;
		this.nome = nome;
		this.rotulo = rotulo;
		this.visivel = visivel;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public boolean isVisivel() {
		return visivel;
	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	@Override
	public String toString() {
		return (this.rotulo!=null)? this.rotulo : this.getNome();
	}
	
	public boolean isCaixaEconomica(){
		if(this.getCodigo()==104){
			return true;
		}
		return false;
	} 

}
