package modules.global.model.dao;

import modules.global.model.entities.core.TipoArvore;

import org.futurepages.core.persistence.Dao;

public class TipoArvoreDao<T extends TipoArvore<T>> extends TipoDao<T> {

	private final static TipoArvoreDao INSTANCE = new TipoArvoreDao(null);
	
	public static TipoArvoreDao getInstance(){
		return INSTANCE;
	}

	public TipoArvoreDao(Class<T> tipo) {
		super(tipo);
	}
	
	public <C extends TipoArvore<T>> TipoArvore<T> get(Class<C> classe, String descricaoCompleta){ 
		return Dao.getInstance().uniqueResult(hql(classe, field("caminho").equalsTo(descricaoCompleta)));
	}
}