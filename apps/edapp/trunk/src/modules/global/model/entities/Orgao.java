package modules.global.model.entities;

import org.futurepages.util.Is;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Collection;

/**
 *  Atenção nos imports...
 *  modules.tjpi.dao.LotacaoDao DEVE SER O ÚNICO ELO DE GLOBAL COM TJPI, POIS GLOBAL NAO DEVERIA NEM REFERENCIAR TJPI. ESTA FOI A SAÍDA TEMPORÁRIA.
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo", "tipo_id"})})
@PrimaryKeyJoinColumn(name = "id")
public class Orgao extends UnidadeOrganizacional implements Serializable {

	private String codigo;
	private String cnpj;
	private String salaNaSede;
	private String andarNaSede;
	private boolean possuiAutonomia;

	@ManyToOne
	private TipoOrgao tipo;

	@ManyToOne
	private Orgao unidadeSuperior;

	@OneToMany(cascade= CascadeType.ALL)
	private Collection<Contato> contatos;

	@OneToOne
	private PessoaFisica gestorAtual;

	@OneToOne
	private PessoaFisica gestorSubstitutoAtual;

	@OneToOne
	private PessoaFisica gestorExcepcionalAtual;

	@OneToOne
	private Predio predioSede;

	public Orgao() {
	}

	public Orgao(String descricao) {
		super(descricao);
		this.possuiAutonomia = false;
	}

	public Orgao(int id, String descricao) {
		super(id,descricao);
		this.possuiAutonomia = false;
	}
	public Orgao(String descricao, boolean comAutonomia) {
		super(descricao);
		this.possuiAutonomia = comAutonomia;
	}

	public Orgao(String descricao, TipoOrgao tipo, Orgao unidadeSuperior) {
		super(descricao);
		this.tipo = tipo;
		this.unidadeSuperior = unidadeSuperior;
	}

	public PessoaFisica getGestorAtual() {
		return gestorAtual;
	}

	public void setGestorAtual(PessoaFisica gestorAtual) {
		this.gestorAtual = gestorAtual;
	}

	public PessoaFisica getGestorSubstitutoAtual() {
		return gestorSubstitutoAtual;
	}

	public void setGestorSubstitutoAtual(PessoaFisica gestorSubstituto) {
		this.gestorSubstitutoAtual = gestorSubstituto;
	}

	public PessoaFisica getGestorExcepcionalAtual() {
		return gestorExcepcionalAtual;
	}

	public void setGestorExcepcionalAtual(PessoaFisica gestorExcepcionalAtual) {
		this.gestorExcepcionalAtual = gestorExcepcionalAtual;
	}

	@Override
	public Orgao getUnidadeSuperior() {
		return unidadeSuperior;
	}

	public void setUnidadeSuperior(Orgao unidadeSuperior) {
		this.unidadeSuperior = unidadeSuperior;
	}

	public String getAndarNaSede() {
		return andarNaSede;
	}

	public void setAndarNaSede(String andarNaSede) {
		this.andarNaSede = andarNaSede;
	}

	public Collection<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(Collection<Contato> contatos) {
		this.contatos = contatos;
	}

	public String getSalaNaSede() {
		return salaNaSede;
	}

	public void setSalaNaSede(String salaNaSede) {
		this.salaNaSede = salaNaSede;
	}

	public Predio getPredioSede() {
		return predioSede;
	}

	public void setPredioSede(Predio predioSede) {
		this.predioSede = predioSede;
	}

	public TipoOrgao getTipo() {
		return tipo;
	}

	public void setTipo(TipoOrgao tipo) {
		this.tipo = tipo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public boolean isPossuiAutonomia() {
		return possuiAutonomia;
	}

	public void setPossuiAutonomia(boolean possuiAutonomia) {
		this.possuiAutonomia = possuiAutonomia;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	// Se o permite lotação for nulo, busca no tipo
	@Override
	public Boolean getPermiteLotacaoDif() {
		if(!Is.empty(getPermiteLotacao())){
			return getPermiteLotacao();
		} else {
			return tipo.getPermiteLotacaoDif();
		}
	}

	/*
	 * Inteligência responsável pela criação dos caminhos
	 */
	@Override
	public String geraCaminho() {
		if (this.getUnidadeSuperior() != null && !this.isPossuiAutonomia()) {
			String caminho = null;
			if (this.getUnidadeSuperior().getTipo().isGrupoConceitual()
				&&(!this.getUnidadeSuperior().isPossuiAutonomia())
				&&(this.getUnidadeSuperior().getUnidadeSuperior() != null)){
						caminho = this.getUnidadeSuperior().getUnidadeSuperior().getCaminho();
			} else {
				caminho = this.getUnidadeSuperior().getCaminho();
			}
			this.setCaminho(caminho + " > " + this.getDescricaoParaCaminho());
		} else {
			this.setCaminho(this.getDescricaoParaCaminho());
		}
		return this.getCaminho();
	}

//    public List<Orgao> getFilhos(){
//        return OrgaoDao.listFilhos(this);
//    }

	public boolean ehSubordinado(Orgao orgao){
		return (this.getCaminho()).startsWith(orgao.getCaminho()+" > ");
	}

	public boolean ehOMesmoOuEhSubordinado(Orgao orgao){
		if(orgao==null){
			return false;
		}
		return this.getId()== orgao.getId() || (this.getCaminho()).startsWith(orgao.getCaminho()+" > ");
	}
}