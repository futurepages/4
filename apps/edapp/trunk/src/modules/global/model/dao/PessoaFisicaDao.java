package modules.global.model.dao;

import modules.global.model.entities.PessoaFisica;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;

/**
 *
 * @author TJPI
 */
public class PessoaFisicaDao extends HQLProvider {
	
	public static PessoaFisica getByLogin(String login) {
		String where = field("user.login").equalsTo(login);
		return Dao.getInstance().uniqueResult(hql(PessoaFisica.class, where));
	}
	
	public static PessoaFisica getByCpf(String cpf) {
		String where = field("cpf").equalsTo(cpf);
		return Dao.getInstance().uniqueResult(hql(PessoaFisica.class, where));
	}

	public static boolean existeComCPF(String cpf) {
		return Dao.getInstance().numRows(hql(PessoaFisica.class, field("cpf").equalsTo(cpf))) > 0;
	}
}
