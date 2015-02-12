package modules.global.model.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import modules.global.model.entities.Pais;
import modules.global.model.entities.brasil.Cidade;
import modules.global.model.dao.CidadeDao;
import modules.global.model.dao.PaisDao;

import org.futurepages.core.persistence.Dao;
import org.futurepages.util.Is;

/**
 *
 * @author wilton
 */
public class CidadeServices {

	public static Cidade criaNovaCidadeEstrangeira(String nomeCidadeEstrangeira, Pais pais) {
		pais = PaisDao.getBySigla(pais.getSigla());
		Cidade cidade = CidadeDao.getCidadeDePaisEstrangeiro(nomeCidadeEstrangeira, pais);

		if (cidade == null) {
			cidade = new Cidade();
			cidade.setNome(nomeCidadeEstrangeira);
			cidade.setNomeBusca(nomeCidadeEstrangeira);
			cidade.setPais(pais);
			cidade.setEstado(null);
			Dao.getInstance().save(cidade);
		}

		return cidade;
	}

	public static String retiraEspacosExtrasDoNome(String inputText) {
		inputText = inputText.trim();

		String[] lista = inputText.split(" ");
		inputText = "";
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].length() > 0) {
				inputText += lista[i] + " ";
			}
		}
		return inputText.substring(0, inputText.length() - 1);
	}

	public static String obtemNomeCidade(String inputText) {
		for (int i = 0; i < inputText.length(); i++) {
			if (inputText.charAt(i) == '-' || inputText.charAt(i) == '(') {
				return inputText.substring(0, i).trim();
			}
		}
		return inputText.trim();
	}

	public static String obtemSiglaEstado(String inputText) {
		for (int i = 0; i < inputText.length(); i++) {
			if (inputText.charAt(i) == '-') {
				return inputText.substring(i + 1, inputText.length()).trim();
			}
		}
		return null;
	}

	public static String obtemNomePais(String inputText) {
		for (int i = 0; i < inputText.length(); i++) {
			if (inputText.charAt(i) == '(') {
				String aux = inputText.substring(i + 1, inputText.length()).trim();
				if (aux.length() > 0 && aux.charAt(aux.length() - 1) == ')') {
					return aux.substring(0, aux.length() - 1);
				}
				return aux;
			}
		}
		return null;
	}

	public static Map<Long, String> mapCidades(String nomeCidade) {
		List<Cidade> cidades = new ArrayList<Cidade>();
        if (!Is.empty(nomeCidade)) {
            nomeCidade = nomeCidade.trim();
            cidades = CidadeDao.listByNomeBusca(nomeCidade);
        }

        Map<Long, String> cidadesMap = new LinkedHashMap<Long, String>();
        for (Cidade cidade : cidades) {
            cidadesMap.put(cidade.getId(), cidade.toString());
        }
		return cidadesMap;
	}

	public static Map<Long, String> mapCidades(String nomeCidade, String estadoSigla) {
		List<Cidade> cidades = new ArrayList<Cidade>();
        if (!Is.empty(nomeCidade)) {
            nomeCidade = nomeCidade.trim();
            cidades = CidadeDao.listByNomeEstadoBusca(nomeCidade,estadoSigla);

        }else{
			cidades = CidadeDao.listByUF(estadoSigla);
		}

        Map<Long, String> cidadesMap = new LinkedHashMap<Long, String>();
        for (Cidade cidade : cidades) {
            cidadesMap.put(cidade.getId(), cidade.getNome());
        }
		return cidadesMap;
	}
}