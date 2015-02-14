package modules.global.model.dao;

import modules.global.model.entities.Banco;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;

import java.util.List;

/**
 *
 * @author Severiano Alves
 */
public class BancoDao extends Dao {

	public static final String DEFAULT_ORDER = concatWithComma(desc("visivel"), asc("nome"));
	
	public static List<Banco> list() {
		return Dao.getInstance().list(hql(Banco.class, field("visivel").isTrue(), asc("nome")));
	}

	public static List<Banco> listComRotulo(){
		return Dao.getInstance().list(hql(Banco.class, ands(field("rotulo").isNotNull(), field("visivel").isTrue()), asc("rotulo")));
	}

	public static PaginationSlice<Banco> paginationSlice(int pageNum, int pageSize, String conteudoBusca) {

		String nome = field("nome").matches(conteudoBusca);

		String where = ors(nome);

		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Banco.class, where, DEFAULT_ORDER));
	}

   public static Banco getByCodigo(int codigo) {
		String whereCodigo = ands(field("codigo").equalsTo(codigo));
		return Dao.getInstance().uniqueResult(hql(Banco.class, whereCodigo));
	}


   	public static Banco getByNome(String nome) {
		String whereNome = ands(field("nome").equalsTo(nome.trim()));
		return Dao.getInstance().uniqueResult(hql(Banco.class,whereNome));
	}

}
