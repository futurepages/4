package modules.global.model.dao;

import java.util.List;
import modules.global.model.entities.PrePessoaFisica;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

/**
 *
 * @author TJPI
 */
public class PrePessoaFisicaDao extends HQLProvider {
	
	public static PrePessoaFisica getByCpf(String cpf) {
		List<PrePessoaFisica> pffs = Dao.getInstance().list(hql(PrePessoaFisica.class, field("cpf").equalsTo(cpf)));
		if(pffs.size()>0){
			return pffs.get(0);
		}
		return null;
	}
}
