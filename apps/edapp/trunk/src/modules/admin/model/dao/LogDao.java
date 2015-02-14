package modules.admin.model.dao;

import java.util.Calendar;
import java.util.List;
import modules.admin.model.entities.Log;
import modules.admin.model.entities.enums.LogType;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

public class LogDao extends HQLProvider {

	public static List<Log> topLastAccessesByUser(int top, String userLogin) {
		return Dao.getInstance().topList(top , hql(Log.class, field("agent").equalsTo(userLogin), desc("dateTime")));
	}

	/**
	 *  Retorna a hora do penúltimo registro de acesso do usuário passado.
	 * @param defUser
	 * @return
	 */
	public static Calendar getFirstLoginBeforeLast(DefaultUser defUser) {
		final String where = ands(field("agent").equalsTo(defUser.getLogin()),field("operType").equalsTo(LogType.LOGIN));
		List<Log> logs = Dao.getInstance().topList(2 , hql(Log.class, where, desc("dateTime")));
		if(logs.size()==2){
			return logs.get(1).getDateTime();
		}
		return null;
	}

    private static String montaWhere(String className, String logType, String agent, String ipHost, String idValue, String logContent) {
        StringBuilder where = new StringBuilder();
        int cont = 0;

        if (!Is.empty(className)) {
            if (cont > 0) {
                where.append(or(field("className").contains(className)));
                cont++;
            } else {
                where.append(field("className").contains(className));
                cont++;
            }
        }
        if (!Is.empty(logType) && !logType.equals("none")) {
            if (cont > 0) {
                where.append(or(field("logType").equalsTo(logType)));
                cont++;
            } else {
                where.append(field("logType").equalsTo(logType));
                cont++;
            }
        }
        if (!Is.empty(agent)) {
            if (cont > 0) {
                where.append(or(field("agent").equalsTo(agent)));
                cont++;
            } else {
                where.append(field("agent").equalsTo(agent));
                cont++;
            }
        }
        if (!Is.empty(ipHost)) {
            if (cont > 0) {
                where.append(or(field("ipHost").equalsTo(ipHost)));
                cont++;
            } else {
                where.append(field("ipHost").equalsTo(ipHost));
                cont++;
            }
        }
        if (!Is.empty(idValue)) {
            if (cont > 0) {
                where.append(or(field("idValue").equalsTo(idValue)));
                cont++;
            } else {
                where.append(field("idValue").equalsTo(idValue));
                cont++;
            }
        }
		if (!Is.empty(logContent)) {
			if (cont > 0) {
				where.append(or(field("logContent").contains(logContent)));
				cont++;
			} else {
				where.append(field("logContent").contains(logContent));
				cont++;
			}
		}

        return where.toString();
    }

    public static PaginationSlice<Log> paginationSliceFiltro(int numPage, int pageSize, String beanName, String logType, String agent, String ipHost, String idValue, String logContent) {
        String where = montaWhere(beanName, logType, agent, ipHost, idValue, logContent);

        return Dao.getInstance().paginationSlice(numPage, pageSize, hql(Log.class, where, desc("dateTime")));
    }

    public static List<Log> list(String beanName, String logType, String agent, String ipHost, String idValue, String logContent) {
        String where = montaWhere(beanName, logType, agent, ipHost, idValue, logContent);

        return Dao.getInstance().list(hql(Log.class, where, desc("dateTime")));
    }
}