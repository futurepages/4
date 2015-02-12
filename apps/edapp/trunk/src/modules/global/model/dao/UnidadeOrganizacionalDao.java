package modules.global.model.dao;

import modules.global.model.entities.UnidadeOrganizacional;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.List;

public class UnidadeOrganizacionalDao extends HQLProvider {

	public static UnidadeOrganizacional getUnidadeByCaminho(String caminho) {
		return Dao.getInstance().uniqueResult(hql(UnidadeOrganizacional.class, ands(OrgaoDao.ATIVO, field("caminho").equalsTo(caminho))));
	}

	public static List<UnidadeOrganizacional> listByDescricaoOrCaminho(String busca) {
		String where = ands(OrgaoDao.ATIVO, OrgaoDao.whereLocalByDescricaoOuCaminho(busca));
		return Dao.getInstance().list(hql(UnidadeOrganizacional.class, where, asc("caminho")));
	}
}