package org.futurepages.core.persistence;

import org.futurepages.util.Is;
import org.futurepages.util.The;

import java.io.Serializable;

/**
 * @author leandro
 */
public class HQLProvider implements HQLable {

	public static HQLField field(String fieldName) {
		return new HQLField(fieldName);
	}

	/*
	 * Como este método é sobrecarregado, sempre que for chamado
	 * com um só parâmetro, o método chamado será o
	 * "HQLField field(String fieldName)"
	 */
	public static HQLField field(String... fieldPath) {
		if (fieldPath.length > 0) {
			StringBuilder sb = new StringBuilder();
			String separator = "";

			if (!Is.empty(fieldPath[0])) {
				sb.append(fieldPath[0]);
				separator = ".";
			}

			for (int i = 1; i < fieldPath.length; i++) {
				if (!Is.empty(fieldPath[i])) {
					sb.append(separator).append(fieldPath[i]);
					separator = ".";
				}
			}

			return new HQLField(sb.toString());
		}

		return null;
	}

	public static String distinct(String selectClause) {
		if (!Is.empty(selectClause)) {
			return " DISTINCT " + selectClause;
		} else {
			return "";
		}
	}

	public static String select(String selectClause) {
		if (!Is.empty(selectClause)) {
			return " SELECT " + selectClause;
		} else {
			return "";
		}
	}

	public static String not(String notClause) {
		if (!Is.empty(notClause)) {
			return concat(" NOT (", notClause, ") ");
		} else {
			return "";
		}
	}

	public static String updateSetting(Class entityClass) {
		return concat(" UPDATE ", entityClass.getName(), " SET ");
	}

	public static String max(String maxClause) {
		if (!Is.empty(maxClause)) {
			return concat(" MAX(", maxClause, ")");
		} else {
			return "";
		}
	}

	public static String min(String minClause) {
		if (!Is.empty(minClause)) {
			return concat(" MIN(", minClause, ")");
		} else {
			return "";
		}
	}

	public static String count(String countClause) {
		if (!Is.empty(countClause)) {
			return concat(" COUNT(", countClause, ")");
		} else {
			return "";
		}
	}

	public static String sum(String sumClause) {
		if (!Is.empty(sumClause)) {
			return concat(" SUM(", sumClause, ")");
		} else {
			return "";
		}
	}

	public static HQLField day(String date) {
		return new HQLField(concat(" DAY(", date, ")"));
	}

	public static HQLField month(String date) {
		return new HQLField(concat(" MONTH(", date, ")"));
	}

	public static HQLField year(String date) {
		return new HQLField(concat(" YEAR(", date, ")"));
	}

	public static HQLField date(String date) {
		return new HQLField(concat(" DATE(", date, ")"));
	}

	public static HQLField size(String element) {
		return new HQLField(concat(" SIZE(", element, ")"));
	}

	public static String from(Class entityClass) {
		if (entityClass != null) {
			return concat(" FROM ", entityClass.getName(), " ");
		} else {
			return "";
		}
	}

	public static String from(Class entityClass, String alias) {
		if (entityClass != null) {
			return concat(" FROM ", entityClass.getName(), " ", alias);
		} else {
			return "";
		}
	}


	/**
	 * @param entityClass
	 * @param alias
	 * @param joins       Joins para a consulta
	 * @return
	 */
	public static String from(Class entityClass, String alias, String... joins) {
		if (entityClass != null) {
			String s = concat(" FROM ", entityClass.getName(), " ", alias);
			StringBuilder sb = new StringBuilder();
			for (String j : joins) {
				sb.append(join(j));
			}
			return concat(s, sb.toString());
		} else {
			return "";
		}
	}


	public static String join(String joinClause) {
		if (!Is.empty(joinClause)) {
			return JOIN + joinClause;
		} else {
			return "";
		}
	}

	public static String leftJoin(String joinClause) {
		if (!Is.empty(joinClause)) {
			return LEFT_JOIN + joinClause;
		} else {
			return "";
		}
	}

	public static String notExists(String clause) {
		if (!Is.empty(clause)) {
			return NOT_EXISTS + "(" + clause + ")";
		} else {
			return "";
		}
	}

	public static String where(String whereClause) {
		if (!Is.empty(whereClause)) {
			return WHERE + whereClause;
		} else {
			return "";
		}
	}

	public static String orderBy(String... fields) {
		if (fields != null) {
			if (fields.length == 1) {
				if (!Is.empty(fields[0])) {
					return ORDER_BY + fields[0];
				}
			} else if (fields.length > 1) {
				StringBuilder sb = new StringBuilder(ORDER_BY);
				for (String field : fields) {
					sb.append(field).append(",");
				}
				String result = sb.toString();
				return result.substring(0, result.length() - 1);
			}
		}
		return "";
	}

	public static String as(String alias) {
		if (!Is.empty(alias)) {
			return AS + alias;
		} else {
			return "";
		}
	}
	public static <T extends Serializable> HQLQuery<T> hql(String field, String expression, Class<T> entityFrom, String where) {
		return new HQLQuery<>(field, expression, entityFrom, where);
	}

	public static <T extends Serializable> HQLQuery<T> hql(Class<T> entityFrom) {
		return new HQLQuery<>(entityFrom);
	}

	public static <T extends Serializable> HQLQuery<T>  hql(Class<T> entityFrom, String where) {
		return new HQLQuery<>(entityFrom, where);
	}

	public static <T extends Serializable> HQLQuery<T> hql(Class<T> entityFrom, String where, String order) {
		return new HQLQuery<>(entityFrom, where, order);
	}

	public static <T extends Serializable> HQLQuery<T> hql(String select, Class<T> entityFrom, String where) {
		return new HQLQuery<>(select, entityFrom, where);
	}

	public static <T extends Serializable> HQLQuery<T> hql(String select, Class<T> entityFrom, String where, String order) {
		return new HQLQuery<>(select, entityFrom, where, order);
	}

	public static <T extends Serializable> HQLQuery<T> hql(String select, Class<T> entityFrom, String alias, String join, String where, String order) {
		return new HQLQuery<>(select,entityFrom,alias,join,where,order);
	}

	public static <T extends Serializable> HQLQuery<T> hql(String select, Class<T> entityFrom, String alias, String join, String where, String group, String having, String order) {
		return new HQLQuery<>(select,entityFrom,alias,join,where,group,having,order);
	}

	public static String asc(String field) {
		return field + ASC;
	}

	public static String desc(String field) {
		return field + DESC;
	}

	/**
	 * Monta conjunções com as expresões passadas
	 *
	 * @param clauses : array de expressões booleanas
	 * @return (clauses[0]) AND (clauses[1]) AND ... AND (clauses[length-1])
	 */
	public static String ands(String... clauses) {
		return connectClauses(AND, clauses);
	}

	public static String commas(String... fields) {
		if (fields != null) {
			if (fields.length == 1) {
				if (!Is.empty(fields[0])) {
					return fields[0].toString();
				}
			} else if (fields.length > 1) {
				StringBuilder sb = new StringBuilder();
				for (Object field : fields) {
					sb.append(field.toString()).append(",");
				}
				String result = sb.toString();
				return result.substring(0, result.length() - 1);
			}
		}
		return "";
	}


	/**
	 * @param clauses
	 * @return
	 */
	private static String connectClauses(String connector, String... clauses) {
		if (clauses == null || clauses.length == 0) {
			return "";
		}
		StringBuffer st = new StringBuffer();
		boolean primeiro = true;
		for (String clause : clauses) {
			if (primeiro) {
				st.append(expressionBuilder("", clause));
				if (!Is.empty(clause)) {
					primeiro = false;
				}
			} else {
				st.append(expressionBuilder(connector, clause));
			}
		}
		return st.toString();
	}

	private static String expressionBuilder(String conector, String clause) {
		if (Is.empty(clause))
			return "";
		return concat(conector, "(", clause, ")");
	}

	public static String and(String clause) {
		if (Is.empty(clause))
			return "";
		return concat(AND, "(", clause, ")");
	}

	public static String ors(String... clauses) {
		return connectClauses(OR, clauses);
	}

	public static String or(String clause) {
		if (Is.empty(clause))
			return "";
		return concat(OR, "(", clause, ")");
	}

	public static String groupBy(String groupClause) {
		if (!Is.empty(groupClause)) {
			return HAVING + groupClause;
		} else {
			return "";
		}
	}

	public static String having(String havingClause) {
		if (!Is.empty(havingClause)) {
			return HAVING + havingClause;
		} else {
			return "";
		}
	}

	public static String concat(String... args) {
		return The.concat(args);
	}
}