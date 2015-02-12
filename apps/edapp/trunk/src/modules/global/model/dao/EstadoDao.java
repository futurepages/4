package modules.global.model.dao;

import modules.global.model.entities.brasil.Estado;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

/**
 *
 * @author diogenes
 */
public class EstadoDao extends HQLProvider {

	public static Estado getBySigla(String estadoSigla) {
		if (!Is.empty(estadoSigla)) {
			return Dao.getInstance().get(Estado.class, estadoSigla);
		}
		return null;
	}
}