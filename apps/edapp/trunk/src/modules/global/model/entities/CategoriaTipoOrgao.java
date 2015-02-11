package modules.global.model.entities;

import modules.global.model.core.CategoriaTipo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Jorge Rafael
 */
@Entity
public class CategoriaTipoOrgao implements CategoriaTipo, Serializable{

	@Id
	private String identificador;

	private String descricao;

	@ManyToMany(fetch=FetchType.LAZY)
	private Collection<TipoOrgao> tipos;

	public CategoriaTipoOrgao(){}

	public CategoriaTipoOrgao(String identificador, String descricao) {
		this.identificador = identificador;
		this.descricao = descricao;
	}

	public CategoriaTipoOrgao(String descricao, Collection<TipoOrgao> tipos) {
		this.descricao = descricao;
		this.tipos = tipos;
	}

	@Override
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Collection<TipoOrgao> getTipos() {
		return tipos;
	}

	public void setTipos(Collection<TipoOrgao> tipos) {
		this.tipos = tipos;
	}
}