package modules.global.model.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modules.global.model.entities.Pais;
import modules.global.model.entities.brasil.Cidade;
import modules.global.model.entities.brasil.Estado;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.The;

/**
 *
 * @author wilton
 */
public class CidadeDao extends HQLProvider {

	public static final String DEFAULT_ORDER = concatWithComma(desc("ativo"), asc("nome"));
	public static final String ATIVO = field("ativo").isTrue();	

	private static String whereBuscaByParam(String nameField, String inputText) {
		return field(nameField).startsWith(The.strWithoutExtraBlanks(inputText));
	}

	
	public static Cidade getCidadeDePaisEstrangeiro(String cidadeEstrangeira, Pais pais) {
		String where = ands(field("nome").equalsTo(cidadeEstrangeira),
				field("pais.sigla").equalsTo(pais.getSigla()),
				field("pais.sigla").differentFrom("BRA"),
				ATIVO);

		return Dao.getInstance().uniqueResult(hql(Cidade.class, where));
	}

	public static Cidade getByNomeBusca(String nomeId) {
		return Dao.getInstance().uniqueResult(hql(Cidade.class, ands(whereBuscaByParam("nomeBusca", nomeId), ATIVO)));
	}


	public static List<Cidade> listByUF(String estadoSigla) {
		return Dao.getInstance().list(hql(Cidade.class, ands(field("estado.sigla").equalsTo(estadoSigla), ATIVO), asc("nome")));
	}

	public static Map<Long, String> mapByEstadoSigla(String estadoSigla) {
		return Dao.getInstance().map(hql("id,nome",Cidade.class, ands(field("estado.sigla").equalsTo(estadoSigla), ATIVO), asc("nome")));
	}

	public static Map<Long, String> mapByName(String inputText) {
		return Dao.getInstance().map(hql("id,nome",Cidade.class, ands(field("nome").startsWith(inputText), ATIVO), asc("nome")));
	}

	public static List<Cidade> listByNomeBusca(String inputText) {

		String where = ands(whereBuscaByParam("nomeBusca", inputText), ATIVO);
		return Dao.getInstance().list(hql(Cidade.class, where, asc("nome")));
	}	

	public static Cidade getById(long cidadeId) {
		return Dao.getInstance().get(Cidade.class, cidadeId);
	}

	public static List<Cidade> listByNomeEstadoBusca(String nomeCidade, String estadoSigla) {
		return Dao.getInstance().list(hql(Cidade.class, ands(ATIVO, whereBuscaByParam("nome", nomeCidade), field("estado.sigla").equalsTo(estadoSigla)), asc("nome")));
	}

	public static Map<Long, String> mapByEstadoSiglaCapitalPrimeiro(String estadoSigla) {
		Map<Long, String> map = new LinkedHashMap<Long, String>();
		Estado estado = EstadoDao.getBySigla(estadoSigla);
		if (estado!=null) {
			Cidade capital = estado.getCapital();
			List<Cidade> cidades = Dao.getInstance().list(hql(Cidade.class, ands(ATIVO, field("estado.sigla").equalsTo(estadoSigla)), asc("nome")));
			map.put(capital.getId(), capital.getNome());
			for (Cidade cidade : cidades) {
				if (cidade != capital) {
					map.put(cidade.getId(), cidade.getNome());
				}
			}
		}
		return map;
	}
	
	public static PaginationSlice<Cidade> paginationSlice(int pageNum, int pageSize, String siglaEstado, String conteudoBusca) {
		String nome = field("nome").hasAnyOfWords(conteudoBusca);	
		String estado = ((siglaEstado == null || siglaEstado.equals("0")) ? "" : field("estado.sigla").endsWith(siglaEstado));
		String[] condicoes = new String[]{estado, nome};
		String where = ands(condicoes);
		
		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(Cidade.class, where, DEFAULT_ORDER));
	}

	public static List<Cidade> listOrderByNome() {
	  	return Dao.getInstance().list(hql(Cidade.class, null, asc("nome")));
	}
	
}