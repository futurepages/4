package modules.admin.model.dao;

import java.util.List;

import modules.admin.model.entities.enums.ParamEnum;
import modules.admin.model.entities.enums.ParamValueType;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import modules.admin.model.entities.Param;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

public class ParamDao extends HQLProvider {

	public static void saveOrUpdate(String paramId, ParamValueType valueType, String val, String titulo, int maxLength) {
		Param param = Dao.getInstance().get(Param.class, paramId);
		if (param == null) {
			persist(paramId, valueType, val, titulo, maxLength);
		} else {
			update(paramId, val);
		}
	}

	public static Param update(String paramId, String val) {
		Param param = Dao.getInstance().get(Param.class, paramId);
		param.setVal(val);
		return Dao.getInstance().update(param);
	}

	public static void persist(String paramId, ParamValueType valueType, String val, String titulo, int maxLength) {
		Param param = new Param(paramId, valueType, val, titulo, maxLength);
		Dao.getInstance().save(param);
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

		return Dao.getInstance().paginationSlice(page, pageSize, hql(Param.class, where.toString(), asc("paramId")));
	}

	public static Param get(String paramId) {
		return Dao.getInstance().get(Param.class, paramId);
	}

	public static Param get(ParamEnum param) {
		return Dao.getInstance().get(Param.class, param.getId());
	}

	public static List<Param> list() {
		return Dao.getInstance().list(hql(Param.class));
	}
}
