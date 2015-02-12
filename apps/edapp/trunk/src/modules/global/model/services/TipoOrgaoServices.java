package modules.global.model.services;

import java.util.Iterator;
import java.util.List;
import modules.global.model.entities.TipoOrgao;
import modules.global.model.dao.TipoOrgaoDao;
import org.futurepages.core.persistence.Dao;

/**
 *
 * @author Daniel
 */
public class TipoOrgaoServices {

	private static void atualizaFilhos(TipoOrgao tipoOrgao){
		List<TipoOrgao> tiposOrgao = TipoOrgaoDao.getFilhos(tipoOrgao.getId());
		Iterator<TipoOrgao> iteratorTiposOrgao = tiposOrgao.iterator();
		TipoOrgao filhoTipoOrgao;
		while(iteratorTiposOrgao.hasNext()){
			filhoTipoOrgao = iteratorTiposOrgao.next();
			atualiza(filhoTipoOrgao);
		}
	}

	public static void atualiza(TipoOrgao tipoOrgao){
		tipoOrgao.geraCaminho();
		Dao.getInstance().update(tipoOrgao);
		atualizaFilhos(tipoOrgao);
	}

}
