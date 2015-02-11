
package modules.global.model.install.auxiliar;

import java.util.ArrayList;
import java.util.List;
import modules.global.model.entities.Contato;
import modules.global.model.entities.Orgao;
import modules.global.model.entities.TipoOrgao;

/**
 *
 * @author Jorge Rafael
 */
public class NoOrgao {
	private static int quant = 0;
	private NoOrgao pai = null;
	private List<NoOrgao> filhos = null;
	private Orgao orgao = null;
	private int nivel = 0;

	public NoOrgao(Orgao orgao) {
		this.orgao = orgao;
		quant++;
		this.orgao.setId(quant);
	}

	public NoOrgao(String descricao) {
		this(new Orgao(descricao));
	}

	public NoOrgao(String descricao, boolean comAutonomia) {
		this(new Orgao(descricao, comAutonomia));
	}

//	public NoOrgao(String descricao, String descricaoAbreviada, TipoOrgao to) {
//		this(descricao);
//		this.orgao.setDescricaoAbreviada(descricaoAbreviada);
//		this.orgao.setTipo(to);
//	}

	public NoOrgao(String descricao, String descricaoAbreviada, TipoOrgao to, boolean autonomia, Boolean permiteLotacao) {
		this(descricao,autonomia);
		this.orgao.setDescricaoAbreviada(descricaoAbreviada);
		this.orgao.setTipo(to);
		this.orgao.setPermiteLotacao(permiteLotacao);
	}
//
//	public NoOrgao(String descricao, String descricaoAbreviada) {
//		this(descricao);
//		this.orgao.setDescricaoAbreviada(descricaoAbreviada);
//	}
//
//	public NoOrgao(String descricao, String descricaoAbreviada, boolean comAutonomia) {
//		this(descricao,comAutonomia);
//		this.orgao.setDescricaoAbreviada(descricaoAbreviada);
//	}

	public int getQuant(){
		return quant;
	}
	
	public List<NoOrgao> getFilhos() {
		return filhos;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public Orgao getOrgao() {
		return orgao;
	}

	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}

	public void setOrgao(String descricao) {
		this.orgao = new Orgao(descricao);
	}

	public NoOrgao getPai() {
		return pai;
	}

	public void setPai(NoOrgao pai) {
		this.pai = pai;
	}

	public NoOrgao filho(String descricao, String descricaoAbreviada){
		Orgao orgaoFilho = new Orgao(descricao);
		orgaoFilho.setDescricaoAbreviada(descricaoAbreviada);
		NoOrgao no = new NoOrgao(orgaoFilho);
		return filho(no);
	}

	public NoOrgao filho(String descricao, String descricaoAbreviada, TipoOrgao to){
		Orgao orgaoFilho = new Orgao(descricao);
		orgaoFilho.setDescricaoAbreviada(descricaoAbreviada);
		orgaoFilho.setTipo(to);
		NoOrgao no = new NoOrgao(orgaoFilho);
		return filho(no);
	}

	public NoOrgao filhos(NoOrgao... filhos) {
		for(NoOrgao no : filhos){
			this.filho(no);
	}
		return this;
}

	public NoOrgao filho(NoOrgao no) {
		return filho(no, null);
	}
	
	public NoOrgao filho(NoOrgao no, ArrayList<Contato> contatos) {
		no.setPai(this);
		if (this.filhos == null) {
			this.filhos = new ArrayList<NoOrgao>();
		}
		no.getOrgao().setContatos(contatos);
		this.filhos.add(no);

		return this;
	}

	@Override
	public String toString() {
		return orgao.getDescricao();
	}
}
