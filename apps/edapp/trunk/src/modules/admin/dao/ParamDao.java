package modules.admin.dao;

import java.util.List;

import org.futurepages.core.persistence.Dao;
import modules.admin.beans.Param;
import modules.admin.enums.ParamEnum;
import modules.admin.enums.ParamValueType;
import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

public class ParamDao extends HQLProvider {

	public static void saveOrUpdate(String paramId, ParamValueType valueType, String val, String titulo, int maxLength) {
		Param param = Dao.get(Param.class, paramId);
		if (param == null) {
			persist(paramId, valueType, val, titulo, maxLength);
		} else {
			update(paramId, val);
		}
	}

	public static Param update(String paramId, String val) {
		Param param = Dao.get(Param.class, paramId);
		param.setVal(val);
		return Dao.update(param);
	}

	public static void persist(String paramId, ParamValueType valueType, String val, String titulo, int maxLength) {
		Param param = new Param(paramId, valueType, val, titulo, maxLength);
		Dao.save(param);
	}

	public static PaginationSlice<Param> paginationSlice(int page, int pageSize, String paramId, String title, String val, String valueType) {
		StringBuilder where = new StringBuilder();
		int cont = 0;

		if (!Is.empty(paramId)) {
			if (cont > 0) {
				where.append(and(field("paramId").contains(paramId)));
			} else {
				where.append(field("paramId").contains(paramId));
			}
			cont++;
		}
		if (!Is.empty(title)) {
			if (cont > 0) {
				where.append(and(field("title").contains(title)));
			} else {
				where.append(field("title").contains(title));
			}
			cont++;
		}
		if (!Is.empty(val)) {
			if (cont > 0) {
				where.append(and(field("val").contains(val)));
			} else {
				where.append(field("val").contains(val));
			}
			cont++;
		}
		if (!Is.empty(valueType) && !valueType.equals("none")) {
			if (cont > 0) {
				where.append(and(field("valueType").contains(valueType)));
			} else {
				where.append(field("valueType").contains(valueType));
			}
			cont++;
		}

		return Dao.paginationSlice(page, pageSize, Param.class, where.toString(), asc("paramId"));
	}

	public static Param get(String paramId) {
		return Dao.get(Param.class, paramId);
	}

	public static Param get(ParamEnum param) {
		return Dao.get(Param.class, param.getId());
	}

	public static List<Param> list() {
		return Dao.list(Param.class);
	}

	/**
	 * Atualiza a variável do pacote do módulo referente ao parâmetro.
	 * @param param
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	// TODO: preciso de uma explanação sobre este método por Leandro
	// Este método era utilizado num sistema da seguinte maneira:
	// - ele atualizava um valor estático (como se fosse cache) numa classe dentro do pacote core do módulo.
	// Não vamos utilizá-lo. Pode desconsiderá-lo. Foi uma abordagem que eu utilizei, mas que eu me arrependi.
	// Não vi grandes ganhos utilizando esta maneira, onde cada parametro está numa classe com o nome do módulo no pacote core.
	// Ideia é criarmos em breve um mecanismo de cache num único mapa inicializado com a aplicação e onde sempre que há um update,
	// o valor é alterado na classe (vamos deixar isto pra depois).
//	public static void updateClassReference(Param param) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
//		if (param.getClassReference() != null) {
//			String[] clsRef = The.explodedToArray(param.getClassReference(), ".");
//			Class classRef = Class.forName(Params.MODULES_PACK+"."+param.getModule().getModuleId()+".core."+clsRef[0]);
//			Field fieldRef = classRef.getDeclaredField(clsRef[1]);
//			if (param.getType().equals("double")) {
//				fieldRef.set(null, Double.parseDouble(param.getParamValue()));
//			}
//			else if (param.getType().equals("int")) {
//				fieldRef.set(null, Integer.parseInt(param.getParamValue()));
//			}
//			else if (param.getType().equals("long")) {
//				fieldRef.set(null, Long.parseLong(param.getParamValue()));
//			}
//			else{
//				fieldRef.set(null, param.getParamValue());
//			}
//		}
//	}
}
