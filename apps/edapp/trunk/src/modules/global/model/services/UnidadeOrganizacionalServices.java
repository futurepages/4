package modules.global.model.services;

import modules.global.model.entities.UnidadeOrganizacional;
import modules.global.model.dao.UnidadeOrganizacionalDao;
import org.futurepages.util.Is;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Criado por Fred em 25/06/2014.
 */
public class UnidadeOrganizacionalServices {
	public static Map<Integer, String> mapUnidades(String caminho) {
		List<UnidadeOrganizacional> unidades = new ArrayList<UnidadeOrganizacional>();
		if (!Is.empty(caminho)) {
			caminho = caminho.trim();
			unidades = UnidadeOrganizacionalDao.listByDescricaoOrCaminho(caminho);
		}

		Map<Integer, String> unidadesMap = new LinkedHashMap<Integer, String>();
		for (UnidadeOrganizacional unidadeOrganizacional : unidades) {
			unidadesMap.put(unidadeOrganizacional.getId(), unidadeOrganizacional.getCaminho());
		}
		return unidadesMap;
	}
}
