
package modules.global.model.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modules.global.model.entities.Endereco;
import modules.global.model.entities.Predio;
import modules.global.model.dao.PredioDao;
import org.futurepages.util.Is;

/**
 *Classe que oferece o serviço de preparação da lista de prédios
 * @author Jefferson
 */
public class PredioServices {

	public static Map<Long, String> mapPredios(String nomePredio) {
		List<Predio> predios = new ArrayList<Predio>();
        if (!Is.empty(nomePredio)) {
            nomePredio = nomePredio.trim();
            predios = PredioDao.listByNomeBusca(nomePredio);
        }

        Map<Long, String> prediosMap = new LinkedHashMap<Long, String>();
        for (Predio predio : predios) {
            prediosMap.put(predio.getId(), predio.toString());
        }
		return prediosMap;
	}
	
	
	public static Predio atualizarPredio(Predio predioTrasiente){
		
		Predio predioPersistente = PredioDao.getById(predioTrasiente.getId());
		
		Endereco enderecoPersistente = predioPersistente.getEndereco();

        predioPersistente.setNome(predioTrasiente.getNome());

		enderecoPersistente.setLogradouro(predioTrasiente.getEndereco().getLogradouro());
		enderecoPersistente.setBairro(predioTrasiente.getEndereco().getBairro());
		enderecoPersistente.setCep(predioTrasiente.getEndereco().getCep());
		enderecoPersistente.setCidade(predioTrasiente.getEndereco().getCidade());

		predioPersistente.setEndereco(enderecoPersistente);
		
		predioPersistente.setNomeBusca(predioTrasiente.toString());
		
		
		return predioPersistente;
		
	}


}
