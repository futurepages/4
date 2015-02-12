package modules.global.model.dao;

import java.util.Map;

import modules.global.model.entities.core.Tipo;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

public class TipoDao<T extends Tipo> extends HQLProvider{

	private Class<T> tipo;

	public TipoDao(Class<T> tipo) {
		this.tipo = tipo;
	}

	public static <T extends Tipo> void remove(Tipo tipo){
		Dao.getInstance().delete(tipo);
	}

	public static <T extends Tipo> T getByChave(Class<T> tipoClass, String fieldName, String chave, boolean visivel){
		if(Is.empty(fieldName)){
			fieldName = "descricao";
		}
		String where = ands(field(fieldName).equalsTo(chave), field("visivel").equalsTo(visivel));
		return Dao.getInstance().uniqueResult(hql(tipoClass, where));
	}

	public Map<String,String> mapByDescricao(String sliceOfText) {
		String where = ands(field("visivel").isTrue(),field("descricao").contains(sliceOfText));
		String ordenacao = asc("descricao");
		return Dao.getInstance().map(hql("descricao,descricao" ,tipo ,	where,	ordenacao));
	}

}