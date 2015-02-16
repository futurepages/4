package modules.global.model.entities;

import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Jorge Rafael
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class UnidadeOrganizacional implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String descricao;

	private String descricaoAbreviada;

	/**
	 * Identificador descritivo único da UnidadeOrganizacional
	 * Geralmente utilizado nas buscas de UnidadeOrganizacional, identificando seu caminho na árvore
	 */
	@Column(unique=true , length=300)
	private String caminho;

	private boolean ativo = true;

	private Boolean permiteLotacao;

	public abstract String geraCaminho();

	public abstract Boolean getPermiteLotacaoDif();

	public abstract UnidadeOrganizacional getUnidadeSuperior();

	public UnidadeOrganizacional() {}

	public UnidadeOrganizacional(String descricao) {
		this.descricao = descricao;
	}
	public UnidadeOrganizacional(int id,String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getPermiteLotacao() {
		return permiteLotacao;
	}


	public void setPermiteLotacao(Boolean permiteLotacao) {
		this.permiteLotacao = permiteLotacao;
	}

	public String getDescricaoParaCaminho(){
		if(!Is.empty(descricaoAbreviada)){
			return descricaoAbreviada;
		}else{
			return descricao;
		}
	}

	public String getDescricaoInstituicao(){
		return The.concat(this.getDescricao(), (this.getDescricaoAbreviada() != null ? The.concat(" - ", this.getDescricaoAbreviada()) : ""));
	}

	public boolean isEhUmTipo(){
		return this instanceof TipoOrgao;
	}

	public String subCaminho(int saltos){
		String[] tokens = this.getCaminho().split(" > ");
		StringBuilder sb = new StringBuilder();

		int i = 0;
		boolean primeiro = true;
		for(String token : tokens){
			if(i>=saltos){
				if(!primeiro){
					sb.append(" > ");
				}else{
					primeiro = false;
				}
				sb.append(token);
			}
			i++;
		}

		return sb.toString();
	}

	public String getGestorAtualLabel() {
		return "Gestor Atual";
	}
}
