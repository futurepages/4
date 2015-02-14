package modules.global.model.dao;

import java.util.List;
import modules.global.model.entities.Predio;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;
import org.futurepages.util.The;

/**
 *
 * @author Jefferson
 */
public class PredioDao extends HQLProvider {

	public static final String DEFAULT_ORDER = concatWithComma(asc("nome"));

	public static List<Predio> list() {
		return Dao.getInstance().list(hql(Predio.class, "", asc("nome")));
	}

	public static PaginationSlice<Predio> paginationSlice(int pageNum, int pageSize, int idCidade, String conteudoBusca) {

		String nome = field("nome").matches(conteudoBusca);

		String logradouro = field("endereco.logradouro").matches(conteudoBusca);

		String bairro = field("endereco.bairro").matches(conteudoBusca);

		String where = ors(nome, logradouro, bairro);

		if (Is.selected(idCidade)) {
			where = ands(where, field("endereco.cidade.id").equalsTo(idCidade));
		}


		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Predio.class, where, DEFAULT_ORDER));
	}

	public static Predio getById(long id) {
		String whereId = ands(field("id").equalsTo(id));
		return Dao.getInstance().uniqueResult(hql(Predio.class, whereId));
	}

	public static List<Predio> listByNomeBusca(String nomePredio) {
		String where = ands(whereBuscaByParam("nomeBusca", nomePredio));
		return Dao.getInstance().list(hql(Predio.class, where, asc("nome")));
	}

	private static String whereBuscaByParam(String nameField, String inputText) {
		return field(nameField).startsWith(The.strWithoutExtraBlanks(inputText));
	}

	public static Predio getPredioByNomeExato(String nomePredio) {
		String where = ands(whereBuscaByParam("nomeBusca", nomePredio));
		return Dao.getInstance().uniqueResult(hql(Predio.class, where));
	}
}
