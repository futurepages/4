package modules.global.model.dao;

import modules.global.model.entities.TipoOrgao;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TipoOrgaoDao extends HQLProvider {

	public static final String DEFAULT_ORDER = concatWithComma(desc("ativo"), asc("descricao"));
	public static final String ATIVO = field("ativo").isTrue();

	public static TipoOrgao getTipoOrgaoById(int id) {
		return Dao.getInstance().get(TipoOrgao.class, id);
	}

	public static TipoOrgao getTipoOrgaoByCaminho(String caminho) {
		return Dao.getInstance().uniqueResult(hql(TipoOrgao.class, ands(ATIVO, field("caminho").equalsTo(caminho))));
	}

	public static PaginationSlice<TipoOrgao> paginationSlice(int pageNum, int pageSize, String campoBusca) {

		String caminho = field("caminho").contains(campoBusca);
		String descricao = field("descricao").contains(campoBusca);
		String[] condicoes = new String[]{caminho, descricao};
		String where = ors(condicoes);

		return Dao.getInstance().paginationSlice(pageNum, pageSize, hql(TipoOrgao.class, where, DEFAULT_ORDER));
	}

	public static Map<Integer, String> carregaUnidadesDoTipoDeOrgao(String inputText) {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		List<TipoOrgao> unidadesSuperiores = Dao.getInstance().list(hql(TipoOrgao.class,
				ands(
					ATIVO,
					ors(field("descricao").contains(inputText),
						field("descricaoAbreviada").contains(inputText),
						field("caminho").contains(inputText))
					),
				asc("descricao")));
		for (TipoOrgao to : unidadesSuperiores) {
			map.put(to.getId(), to.getCaminho());
		}
		return map;
	}

	public static List<TipoOrgao> getFilhos(int idTipoOrgaoPai){
		return Dao.getInstance().list(hql(TipoOrgao.class, field("unidadeSuperior.id").equalsTo(idTipoOrgaoPai)));
	}

	public static List<TipoOrgao> listAtivos() {
		return Dao.getInstance().list(hql(TipoOrgao.class, ATIVO, DEFAULT_ORDER));
	}
}