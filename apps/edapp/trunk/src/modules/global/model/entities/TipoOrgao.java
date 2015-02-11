package modules.global.model.entities;

//import modules.global.model.dao.OrgaoDao;

import org.futurepages.util.Is;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Daiane Algarves
 */
@Entity
@PrimaryKeyJoinColumn(name="id")
public class TipoOrgao extends UnidadeOrganizacional implements Serializable{

	private boolean permiteMembros;

	private boolean caraterPermanente;

	private boolean assessoria;

	private boolean grupoConceitual;

	private String adjetivoGestor; //criado para uso futuro, não está sendo utilizado ainda

	private String adjetivoSubgestor; //criado para uso futuro, não está sendo utilizado ainda

	@ManyToMany(fetch=FetchType.LAZY)
	private Collection<CategoriaTipoOrgao> categorias;

	@ManyToOne
	private TipoOrgao unidadeSuperior;

    public TipoOrgao() {

	}

	public TipoOrgao(String descricao) {
		setDescricao(descricao);
	}

	public TipoOrgao(String descricao, String descricaoAbreviada, Boolean permiteLotacao, boolean permiteMembros, boolean caraterPermanente, boolean assessoria, boolean grupoConceitual, String adjetivoGestor, String adjetivoSubgestor) {
		super(descricao);
		setDescricaoAbreviada(descricaoAbreviada);
		setPermiteLotacao(permiteLotacao);
		this.permiteMembros = permiteMembros;
		this.caraterPermanente = caraterPermanente;
		this.assessoria = assessoria;
		this.grupoConceitual = grupoConceitual;
		this.adjetivoGestor = adjetivoGestor;
		this.adjetivoSubgestor = adjetivoSubgestor;
	}

	public boolean isPermiteMembros() {
		return permiteMembros;
	}

	public void setPermiteMembros(boolean permiteMembros) {
		this.permiteMembros = permiteMembros;
	}

	public boolean isCaraterPermanente() {
		return caraterPermanente;
	}

	public void setCaraterPermanente(boolean caraterPermanente) {
		this.caraterPermanente = caraterPermanente;
	}

	public boolean isAssessoria() {
		return assessoria;
	}

	public void setAssessoria(boolean assessoria) {
		this.assessoria = assessoria;
	}

	public boolean isGrupoConceitual() {
		return grupoConceitual;
	}

	public void setGrupoConceitual(boolean grupoConceitual) {
		this.grupoConceitual = grupoConceitual;
	}

	public String getAdjetivoGestor() {
		return adjetivoGestor;
	}

	public void setAdjetivoGestor(String adjetivoGestor) {
		this.adjetivoGestor = adjetivoGestor;
	}

	public String getAdjetivoSubgestor() {
		return adjetivoSubgestor;
	}

	public void setAdjetivoSubgestor(String adjetivoSubgestor) {
		this.adjetivoSubgestor = adjetivoSubgestor;
	}

	public Collection<CategoriaTipoOrgao> getCategorias() {
		return categorias;
	}

	public void setCategorias(Collection<CategoriaTipoOrgao> categorias) {
		this.categorias = categorias;
	}

	@Override
	public TipoOrgao getUnidadeSuperior() {
		return unidadeSuperior;
	}

	public void setUnidadeSuperior(TipoOrgao tipoSuperior) {
		this.unidadeSuperior = tipoSuperior;
	}

	@Override
	public Boolean getPermiteLotacaoDif() {
		if (Is.empty(getPermiteLotacao())) { 
		   throw new NullPointerException("O valor de Permite Lotação encontra-se nulo.");
		}
		return getPermiteLotacao();
	}
	
	@Override
	public String geraCaminho() {
		if(this.getUnidadeSuperior() != null ){
				this.setCaminho(this.getUnidadeSuperior().getCaminho()+" > "+this.getDescricaoParaCaminho());
		}else{
			this.setCaminho(this.getDescricaoParaCaminho());
		}
		
		return this.getCaminho();
	}

//	public long getQuantidadeOrgaos(){
//		return OrgaoDao.quantidadeOrgaosPorTipoOrgao(this.getId());
//	}
}