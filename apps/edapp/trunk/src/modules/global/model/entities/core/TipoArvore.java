package modules.global.model.entities.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

//import modules.global.model.dao.TipoArvoreDao;

import org.futurepages.core.persistence.Dao;
import org.futurepages.util.The;

@MappedSuperclass
@AttributeOverrides({
	@AttributeOverride(name = "descricao", column =	@Column(name = "descricao", unique = false))
})
public abstract class TipoArvore<T extends TipoArvore> extends TipoComposto implements Serializable {

	private Boolean folha;
	
	@ManyToOne
	private T conjunto;

	@OneToMany(fetch= FetchType.LAZY,mappedBy="conjunto")
	private Set<T> filhos;
	
	public TipoArvore() {
	}

	@Override
	/**
	 * Identifica um possíveis super conjuntos no descritor.
	 */
	public void build(String descritor) {
//		setCaminho(descritor);
//		String[] partes = getCaminhoFatiado();
//
//		T novoConjunto = null;
//		if (partes.length > 1) {
//			setDescricao(partes[partes.length - 1]);
//			try {
//				StringBuilder buf = new StringBuilder();
//				for (int i = 0; i < partes.length - 1; i++) {
//					if (i != 0) {
//						buf.append(DELIMITADOR);
//					}
//					buf.append(partes[i]);
//				}
//				String descricaoConjunto = buf.toString();
//				novoConjunto = buildConjunto(descricaoConjunto);
//			} catch (Exception e) {
//				throw new RuntimeException(e.getCause());
//			}
//		} else {
//			setDescricao(descritor);
//		}
//		this.setConjunto(novoConjunto);
	}

//	private T buildConjunto(String caminhoConjunto) throws InstantiationException, IllegalAccessException {
//		T novoConjunto = (T) new TipoArvoreDao(this.getClass()).get(this.getClass(), caminhoConjunto);
//		if (novoConjunto == null) {
//			novoConjunto = (T) this.getClass().newInstance();
//			novoConjunto.build(caminhoConjunto);
//			Dao.save(novoConjunto);
//		}
//		return novoConjunto;
//	}

	public T getConjunto() {
		return conjunto;
	}

	public void setConjunto(T conjunto) {
		this.conjunto = conjunto;
	}

	public Boolean getFolha() {
		return folha;
	}

	public void setFolha(Boolean folha) {
		this.folha = folha;
	}
	
	public Set<T> getFilhos() {
		return filhos;
	}

	public void setFilhos(Set<T> filhos) {
		this.filhos = filhos;
	}

	/**
	 * Por Exemplo: descricaoCompleta: Civil > Obrigações > Execução Coativa
	 * Retorna a area + a descricao dele que no caso é: (Civil) Execução Coativa
	 */
	public String getResumoDescricao() {

		if (this.getConjunto() == null) {
			return "(" + getCaminho() + ")";
		}
		return "(" + getDescricaoRaiz() + ") " + getDescricao();
	}

	/**
	 * Por Exemplo: descricaoCompleta é: Civil > Obrigações > Execução Coativa
	 * Retorna a área dele que no caso é: Civil
	 */
	public String getDescricaoRaiz() {
		if (this.getConjunto() == null) {
			return this.getDescricao();
		}
		return The.firstTokenOf(this.getCaminho(), ">").trim();
	}

	public String getCaminhoInternoResumido() {
		String[] nodes = The.explodedToArray(getCaminho(), ">");
		StringBuilder sb = new StringBuilder("");
		if (nodes.length > 2) {
			int limitante =  (nodes.length == 3 ? 2 : 3);
			for (int i = 1; i < limitante; i++) {
				sb.append(nodes[i].trim()+((i<limitante-1)? " &raquo; " : ""));
			}
			if(nodes.length == 4) {
				sb.append(nodes[3]);
			}
			if (nodes.length > 4) {
				sb.append("... &raquo;");
				sb.append(nodes[nodes.length-2]);
			}
		}
		return sb.toString();
	}
	
	public List<String> getListCaminhoInterno() {
		String[] nodes = The.explodedToArray(getCaminho(), ">");
		ArrayList list  = new ArrayList();
		for(int i = 1 ; i < nodes.length-1 ; i++){
			list.add(nodes[i]);
		}
		return list;
	}
}