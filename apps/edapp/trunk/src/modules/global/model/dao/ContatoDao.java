package modules.global.model.dao;

import modules.global.model.entities.Contato;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

/**
 *
 * @author Fred
 */
public class ContatoDao extends HQLProvider {
	
	public static final String DEFAULT_ORDER = concatWithComma(desc("ativo"), asc("nome"));
	public static final String ATIVO = field("ativo").isTrue();		

	public static Contato getContatoById(int id){
		return Dao.getInstance().get(Contato.class, id);
	}
	
	public static boolean existeContatoByEmail(String email) {
		String where = field("valor").equalsTo(email);
		return Dao.getInstance().numRows(hql(Contato.class, where)) > 0;
	}
	
	public static PaginationSlice<Contato> paginationSlice(int pageNum, int pageSize, String conteudoBusca) {
		String nome = field("valor").hasAnyOfWords(conteudoBusca);			
		String[] condicoes = new String[]{nome};
		String where = ands(condicoes);
		
		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Contato.class, where, DEFAULT_ORDER));
	}
}