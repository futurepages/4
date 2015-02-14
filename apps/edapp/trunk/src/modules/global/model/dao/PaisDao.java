package modules.global.model.dao;

import modules.global.model.entities.Pais;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.List;

public class PaisDao extends HQLProvider {

	public static final String DEFAULT_ORDER = concatWithComma(desc("ativo"), asc("nome"));
	public static final String ATIVO = field("ativo").isTrue();

	public static Pais getBySigla(String sigla) {
		String whereSigla = ands(ATIVO, field("sigla").equalsTo(sigla));
		return Dao.getInstance().uniqueResult(hql(Pais.class, whereSigla));
	}

	public static Pais getByNome(String nome) {
		String whereSigla = ands(ATIVO, field("nome").equalsTo(nome.trim()));
		return Dao.getInstance().uniqueResult(hql(Pais.class, whereSigla));
	}

	/**
	 * Lista todos ordenado por nome
	 * @param whereExcecao excluir da listagem algum registro
	 */
	public static List<Pais> listOrderByNome(String whereExcecao) {
		return Dao.getInstance().list(hql(Pais.class, whereExcecao, asc("nome")));
	}

	public static PaginationSlice<Pais> paginateList(int pageNum, int pageSize, String conteudoBusca) {
		String sigla = field("sigla").hasAnyOfWords(conteudoBusca);
		String nome = field("nome").hasAnyOfWords(conteudoBusca);

		String where = ors(sigla, nome);

		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Pais.class, where, DEFAULT_ORDER));
	}
}
