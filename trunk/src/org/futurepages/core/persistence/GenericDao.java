package org.futurepages.core.persistence;

import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.util.Is;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenericDao extends HQLProvider {

	private String schemaId;


	GenericDao(){ this.schemaId = HibernateManager.DEFAULT; }

	GenericDao(String sessionFactoryKey){
		this.schemaId = sessionFactoryKey;
	}

	public Session session() {
		return HibernateManager.getSession(schemaId);
	}

	public void clearSession() {
		Session session = session();
		if (session.isDirty()) {
			session.clear();
		}
	}

	public void open() {
		if (!session().isOpen()) {
			session().getSessionFactory().openSession();
		}
	}

	public void close() {
		if (session().isOpen()) {
			session().close();
		}
	}

	public <T extends Serializable> void evict(Class<T> entity){
		session().getSessionFactory().evict(entity);
	}

	public Criteria createCriteria(Class entityClass) {
		return session().createCriteria(entityClass);
	}

	public SQLQuery sqlQuery(String sqlQuery) {
//		System.out.println("SQL: "+sqlQuery); //para depurar SQls, descomente. // for DEBUG-MODE.
		return session().createSQLQuery(sqlQuery);
	}

	public SQLQuery sqlQuery(String sqlQuery, Class classe) {
//		System.out.println("SQL: "+sqlQuery); //para depurar SQls, descomente. // for DEBUG-MODE.
		return session().createSQLQuery(sqlQuery).addEntity(classe);
	}

	public Query query(String hqlQuery) {
//		System.out.println("HQL: "+hqlQuery); //para depurar HQLs, descomente. // for DEBUG-MODE.
		Query query = session().createQuery(hqlQuery);//.setCacheable(true);
		return query;
	}

	public <T extends Serializable> List<T> sqlQueryList(String sql, Class<T> clss) {
		SQLQuery query = sqlQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(clss));
		return query.list();
	}

	public <T extends Serializable> List<T> sqlList(String sql, Class<T> clss) {
		SQLQuery query = sqlQuery(sql, clss);
		return query.list();
	}

	public String getIdName(Class entity) {
		ClassMetadata classMetadata = session().getSessionFactory().getClassMetadata(entity);
		if (classMetadata == null) {
			return null;
		}
		return classMetadata.getIdentifierPropertyName();
	}

	public Class getIdType(Class entity) {
		return session().getSessionFactory().getClassMetadata(entity).getIdentifierType().getClass();

	}

	public boolean isTransactionActive() {
		return session().getTransaction().isActive();
	}

	public Object uniqueResult(String function, Class entity, String where) {
		Object obj = query(select(function) + from(entity) + where(where)).uniqueResult();
		return obj;
	}

	public Object getMinField(String field, Class entity, String where) {
		Object result = uniqueResult(min(field), entity, where);
		return result;
	}

	public Object getMaxField(String field, Class entity, String where) {
		Object result = uniqueResult(max(field), entity, where);
		return result;
	}

	public long getNextLong(String field, Class entity) {
		Long newId = (Long) uniqueResult(max(field) + "+1", entity, null);
		if (newId != null) {
			return newId;
		}
		return 1;
	}

	public Object updateField(Class entity, String field, String expression, String whereClause) {
		String hql = concat(updateSetting(entity), field, EQUALS, expression, where(whereClause));
		query(hql).executeUpdate();
		return query(select(field) + from(entity) + where(whereClause)).uniqueResult();
	}

	public Object incrementField(Class entity, String field, String whereClause, Integer quantidade) {
		return updateField(entity, field, field + "+(" + quantidade + ")", whereClause);
	}

	public Object decrementField(Class entity, String field, String whereClause, Integer quantidade) {
		return updateField(entity, field, field + "-(" + quantidade + ")", whereClause);
	}

	public Object incrementField(Class entity, String field, String whereClause) {
		return incrementField(entity, field, whereClause, 1);
	}

	public Object decrementField(Class entity, String field, String whereClause) {
		return updateField(entity, field, field + "-1", whereClause);
	}

	public void delete(Class entity, String alias, String whereClause) {
		String hql = concat(DELETE, from(entity), as(alias),  where(whereClause));
		query(hql).executeUpdate();
	}

	public long getNextLongId(Class entity) {
		Long newId = (Long) uniqueResult(max(getIdName(entity)) + "+1", entity, null);
		if (newId != null) {
			return newId;
		}
		return (long) 1;
	}

	public int getNextIntId(Class entity) {
		Integer newId = (Integer) uniqueResult(max(getIdName(entity)) + "+1", entity, null);
		if (newId != null) {
			return newId;
		}
		return (int) 1;
	}

	public <T extends Serializable> List<T> list(Class<T> entity) {
		return list(entity, null);
	}

	public <T extends Serializable> List<T> listWithJoin(Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return listWithJoin(entity.getSimpleName().toLowerCase(), entity, joinClause, whereClause, orderClauses);
	}

	public <T extends Serializable> List<T> listWithJoin(String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
		Query query = query(select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return query.list();
	}

	public <T extends Serializable> List<T> listDistinctWithJoin(String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
		Query query = query(select(distinct(entityAlias)) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return query.list();
	}


	public <T extends Serializable> List<T> listDistinctWithJoin(String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		Query query = query(select(distinct(entityAlias)) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return query.list();
	}

	public <T extends Serializable> List<T> list(String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		Query query = query(select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return query.list();
	}

	public <T extends Serializable> List<T> list(Class<T> entity, String whereClause, String... orderClauses) {
		return list(concat(from(entity), where(whereClause), orderBy(orderClauses)));
	}

	public <T extends Serializable> List<T> list(String hqlQuery) {
		Query query = query(hqlQuery);
		return (List<T>) query.list();
	}

	public <T> List<T> list(String hqlQuery, Class<T> resultClass) {
		Query query = query(hqlQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(resultClass));
		return (List<T>) query.list();
	}

	public <T> List<T> topList(int topSize, String hqlQuery, Class<T> resultClass) {
		Query query = query(hqlQuery);
		query.setMaxResults(topSize);
		query.setResultTransformer(new AliasToBeanResultTransformer(resultClass));
		return (List<T>) query.list();

	}

	private int correctPageNumber(int page, int totalPages, int pagesOffset) {
		if(pagesOffset == 0){
			if (page > totalPages) {
				page = totalPages;
			}
		}
		return page;
	}

	private int calcNumPages(final long numRows, int pageSize) {
		int totalPages = (int) Math.ceil(numRows / (double) pageSize);
		return totalPages;
	}

	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize,                  Class<T> entity, String whereClause, String... orderClauses) {
		return paginationSlice(page, pageSize, 0, entity, whereClause, orderClauses);
	}
	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, Class<T> entity, String whereClause, String... orderClauses) {
		final long numRows = numRows(entity, whereClause);
		int totalPages = calcNumPages(numRows, pageSize);
		page = correctPageNumber(page, totalPages, pagesOffset);
		List<T> list = listPage(page, pageSize, pagesOffset, concat(from(entity), where(whereClause), orderBy(orderClauses)));
		return new PaginationSlice<T>(numRows, pageSize, pagesOffset, totalPages, page, list);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize,                  String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return paginationSlice(page, pageSize, 0, entityAlias,fromAndJoin, whereClause, orderClauses);
	}
	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		final long numRows = numRows(fromAndJoin, whereClause);
		int totalPages = calcNumPages(numRows, pageSize);
		page = correctPageNumber(page, totalPages, pagesOffset);
		List<T> list = listPage(page, pageSize, pagesOffset, select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return new PaginationSlice<T>(numRows, pageSize, pagesOffset, totalPages, page, list);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSliceWithDistinct(int page, int pageSize,                  String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return paginationSliceWithDistinct(page, pageSize, 0, entityAlias,fromAndJoin, whereClause, orderClauses);
	}
	public <T extends Serializable> PaginationSlice<T> paginationSliceWithDistinct(int page, int pageSize, int pagesOffset, String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		final long numRows = numRowsWithDistinct(entityAlias, fromAndJoin, whereClause);
		int totalPages = calcNumPages(numRows, pageSize);
		page = correctPageNumber(page, totalPages, pagesOffset);
		List<T> list = listPage(page, pageSize, pagesOffset, select(distinct( entityAlias)) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return new PaginationSlice<T>(numRows, pageSize, pagesOffset, totalPages, page, list);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, int pagesOffset, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return paginationSliceWithJoin(page, pageSize, pagesOffset, entity.getSimpleName().toLowerCase(), entity, joinClause, whereClause, orderClauses);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, int pagesOffset, String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
		final long numRows = numRows(fromAndJoin, whereClause);
		int totalPages = calcNumPages(numRows, pageSize);

		page = correctPageNumber(page, totalPages,pagesOffset);
		List<T> list = listPage(page, pageSize, pagesOffset, select(entityAlias) + fromAndJoin + where(whereClause) + orderBy(orderClauses));
		return new PaginationSlice<T>(numRows, pageSize, pagesOffset, totalPages, page, list);
	}



	public long numRowsWithDistinct(String entityAlias, String fromClause, String where) {
		Long res = (Long) query(concat(select(count(distinct(entityAlias))), fromClause, where(where))).uniqueResult();
		return res.longValue();
	}

	private <T> String fromAndJoin(String entityAlias, Class<T> entity, String joinClause) {
		return from(entity) + as(entityAlias) + (joinClause != null ? join(joinClause) : "");
	}

	public <T extends Serializable> List<T> listPage(int page, int pageSize, int pagesOffset, String hqlQuery) {
		Query query = query(hqlQuery);
		return slicedQuery(query,page,pageSize,pagesOffset).list();
	}

	public <T extends Serializable> List<T> reportPage(int page, int pageSize, int offset, Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		Query query = query(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return slicedQuery(query,page,pageSize,offset).list();
	}

	/**
	 * the slice of the query to be retrieved
	 */
	private Query slicedQuery(Query query, int pageNumber, int pageSize, int pagesOffset){
		query.setFirstResult(((pageNumber * pageSize) - pageSize)+pagesOffset);
		query.setMaxResults(pageSize);
		return query;
	}


	/* TOP LIST*/
	public <T extends Serializable> List<T> topListWithJoin(int topSize, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return topListWithJoin(topSize, entity.getSimpleName().toLowerCase(), entity, joinClause, whereClause, orderClauses);
	}

	public <T extends Serializable> List<T> topListWithJoin(int topSize, String entityAlias, Class<T> entity, String joinClause, String where, String... order) {
		String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
		String strQuery = concat(select(entityAlias), fromAndJoin, where(where), orderBy(order));
		Query query = query(strQuery);
		query.setMaxResults(topSize);
		return query.list();

	}

	public <T extends Serializable> List<T> topList(int topSize, String entityAlias, String fromAndJoin, String where, String... order) {
		String strQuery = concat(select(entityAlias), fromAndJoin, where(where), orderBy(order));
		Query query = query(strQuery);
		query.setMaxResults(topSize);
		return query.list();

	}

	public <T extends Serializable> List<T> topList(int topSize, Class<T> entity, String where, String... order) {
		Query query = query(concat(from(entity), where(where), orderBy(order)));
		query.setMaxResults(topSize);
		return query.list();
	}

	public <T extends Serializable> List<T> topList(int topSize, String hqlQuery) {
		Query query = query(hqlQuery);
		query.setMaxResults(topSize);
		return query.list();
	}

	public <T extends Serializable> List<T> listReports(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		Query query = query(concat(select(fields), from(entity), where(where), groupBy(group), orderBy(order)));
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return query.list();
	}

	public <T extends Serializable> T getReport(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		Query query = query(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return (T) query.uniqueResult();
	}

	public <T extends Serializable> LinkedHashMap map(Class<T> entity, String key, String value, String where, String... order) {
		Query query = query(concat(select(key + "," + value), from(entity), where(where), orderBy(order)));
		List<Object[]> results = query.list();
		LinkedHashMap map = new LinkedHashMap();
		for (Object[] result : results) {
			map.put(result[0], result[1]);
		}
		return map;
	}

	public <T extends Serializable> LinkedHashMap mapGrouped(Class<T> entity, String key, String value, String where, String... order) {
		Query query = query(select(key + "," + value) + from(entity) + where(where) + groupBy(key) + orderBy(order));
		List<Object[]> results = query.list();
		LinkedHashMap map = new LinkedHashMap();
		for (Object[] result : results) {
			map.put(result[0], result[1]);
		}
		return map;
	}

	public <T extends Serializable> Map mapGrouped(Class<T> entity, String entityAlias, String join, String key, String value, String where) {
		Query query = query(select(key + "," + value) + from(entity) + as(entityAlias) + join(join) + where(where) + groupBy(key));
		List<Object[]> results = query.list();
		Map map = new HashMap();
		for (Object[] result : results) {
			map.put(result[0], result[1]);
		}
		return map;
	}

	public Map mapGrouped(String entityAlias, String fromAndjoin, String key, String value, String where) {
		Query query = query(select(key + "," + value) + fromAndjoin + where(where) + groupBy(key));
		List<Object[]> results = query.list();
		Map map = new HashMap();
		for (Object[] result : results) {
			map.put(result[0], result[1]);
		}
		return map;
	}

	public Object reportTotal(Class entity, String functions, String where, Class reportClass) {
		Query query = query(select(functions) + from(entity) + where(where));
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return query.uniqueResult();
	}

	public <T extends Serializable> List<T> topReportWithJoin(int topSize, Class entity,
	                                                          Class<T> reportClass, String fields, String joinClause, String whereClause, String group, String... orderClauses) {

		return topReportWithJoin(topSize, entity.getSimpleName().toLowerCase(), entity,
				reportClass, fields, joinClause, whereClause, group, orderClauses);
	}

	public <T extends Serializable> List<T> topReportWithJoin(int topSize, String entityAlias, Class entity,
	                                                          Class<T> reportClass, String fields, String joinClause, String whereClause, String group, String... orderClauses) {

		String fromAndJoin = fromAndJoin(entityAlias, entity, joinClause);
		String strQuery = select(fields) + fromAndJoin + where(whereClause) + groupBy(group) + orderBy(orderClauses);
		Query query = query(strQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		query.setMaxResults(topSize);
		return query.list();
	}

	public <T extends Serializable> List<T> topReport(int topSize, String entityAlias, Class entity,
	                                                  Class<T> reportClass, String fields, String fromAndJoin, String whereClause, String group, String... orderClauses) {

		String strQuery = select(fields) + fromAndJoin + where(whereClause) + groupBy(group) + orderBy(orderClauses);
		Query query = query(strQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		query.setMaxResults(topSize);
		return query.list();
	}

	public <T extends Serializable> List<T> topReport(Integer topSize, Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		Query query = query(select(fields) + from(entity) + where(where) + groupBy(group) + orderBy(order));
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		if (topSize != null) {
			query.setMaxResults(topSize);
		}
		return query.list();
	}

	public <T extends Serializable> List<T> report(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		return topReport(null, entity, reportClass, fields, where, group, order);
	}

	public long numRows(Class entity, String where) {
		Long res = (Long) query(concat(select(count("*")), from(entity), where(where))).uniqueResult();
		return res.longValue();
	}

	public long numRows(String fromClause, String where) {
		Long res = (Long) query(concat(select(count("*")), fromClause, where(where))).uniqueResult();
		return res.longValue();
	}

	public long numRows(String whereClause, String group, Class entity) {
		Long res = (Long) query(concat(select(count(distinct(group))), from(entity), where(whereClause))).uniqueResult();
		return res.longValue();
	}

	public long numRows(String hqlQuery){
		Long res = (Long) query(hqlQuery).uniqueResult();
		return res.longValue();
	}

	/**
	 * /////////////////////////////////////////////////
	 * MÉTODOS TRANSACIONAIS
	 * /////////////////////////////////////////////////
	 */
	public void beginTransaction() {
		session().getTransaction().begin();
	}

	public void rollBackTransaction() {
		session().getTransaction().rollback();
	}

	public void commitTransaction() {
		session().getTransaction().commit();
	}

	public void flush() {
		session().flush();
	}

	public <T extends Serializable> T saveTransaction(T obj) {
		beginTransaction();
		session().save(obj);
		commitTransaction();
		return obj;
	}

	public <T extends Serializable> Collection<T> saveTransaction(Collection<T> objs) {
		beginTransaction();
		for (T obj : objs) {
			save(obj);
		}
		commitTransaction();
		return objs;
	}

	public <T extends Serializable> T updateTransaction(T obj) {
		beginTransaction();
		session().update(obj);
		commitTransaction();
		return obj;
	}

	public <T extends Serializable> T saveOrUpdateTransaction(T obj) {
		beginTransaction();
		session().saveOrUpdate(obj);
		commitTransaction();
		return obj;
	}

	public <T extends Serializable> T deleteTransaction(T obj) {
		beginTransaction();
		session().delete(obj);
		commitTransaction();
		return obj;
	}

	public <T extends Serializable> void deleteTransaction(Collection<T> objs) {
		beginTransaction();
		for (T obj : objs) {
			delete(obj);
		}
		commitTransaction();
	}

	public <T extends Serializable> T save(T obj) {
		session().save(obj);
		return obj;
	}

	public <T extends Serializable> void save(T[] arr) {
		for (T obj : arr) {
			save(obj);
		}
	}

	public <T extends Serializable> void save(Collection<T> objs) {
		for (T obj : objs) {
			save(obj);
		}
	}

	public <T extends Serializable> T update(T obj) {
		session().update(obj);
		return obj;
	}

	public <T extends Serializable> void update(T[] arr) {
		for (T obj : arr) {
			session().update(obj);
		}
	}

	public <T extends Serializable> void update(Collection<T> objs) {
		for (T obj : objs) {
			update(obj);
		}
	}

	public <T extends Serializable> T delete(T obj) {
		session().delete(obj);
		return obj;
	}

	public <T extends Serializable> void delete(T[] arr) {
		for (T obj : arr) {
			session().delete(obj);
		}
	}

	public <T extends Serializable> void delete(Collection<T> objs) {
		for (T obj : objs) {
			delete(obj);
		}
	}

	public <T extends Serializable> T saveOrUpdate(T obj) {
		session().saveOrUpdate(obj);
		return obj;
	}

	public synchronized <T extends Serializable> T syncSaveOrUpdate(T obj) {
		session().saveOrUpdate(obj);
		return obj;
	}

	public <T extends Serializable> T get(Class<T> entity, Serializable id) {
		return (T) session().get(entity, id);
	}

	public <T> T uniqueResult(Class<T> entity, String where) {
		return (T) query(from(entity) + where(where)).uniqueResult();

	}

	public <T> T uniqueResult(String hqlQuery) {
		Query query = query(hqlQuery);
		return (T) query.uniqueResult();
	}

	public <T extends Serializable> T load(Class<T> entity, Serializable id) {
		return (T) session().load(entity, id);
	}

	public <T extends Serializable> void evict(T obj) {
		session().evict(obj);
	}

	public <T extends Serializable> void persist(T obj) {
		session().persist(obj);
	}

	public <T extends Serializable> T refresh(T obj) {
		session().refresh(obj);
		return obj;
	}

	public <T extends Serializable> T merge(T obj) {
		session().merge(obj);
		return obj;
	}

	public void executeSQL(String sql) {
		if (!Is.empty(sql)) {
			sqlQuery(sql).executeUpdate();
		}
	}

	public Object getSQL(String sql) {
		if (!Is.empty(sql)) {
			return sqlQuery(sql).uniqueResult();
		}else{
			return null;
		}
	}

	public void executeSQLs(String... sqls) {
		executeSQLs(false, sqls);
	}

	//TODO CORRIGIR BUG QUANDO   /* ... [\n]* ... */
	public void executeSQLs(boolean withLog, String... sqls) {
		String trimmedSql = null;
		String delimiter = ";";
		StringBuffer sqlToExecute = new StringBuffer();

		for (int i = 0; i < sqls.length; i++) {
			trimmedSql = sqls[i].trim();
			if (trimmedSql.length() == 0
					|| trimmedSql.startsWith("--")
					|| trimmedSql.startsWith("//")
					|| trimmedSql.startsWith("#")
					|| (trimmedSql.startsWith("/*") && !trimmedSql.startsWith("/*!") )) {
				continue;
			} else {
				int posComment = trimmedSql.indexOf("--");
				if(posComment>0){
					trimmedSql = trimmedSql.substring(0, posComment);
				}
				if (trimmedSql.length() > 10 && trimmedSql.substring(0, 10).toLowerCase().equals("delimiter ")) {
					delimiter = trimmedSql.substring(10);
				} else {
					if (i == sqls.length - 1 && !trimmedSql.endsWith(delimiter)) {
						sqlToExecute.append(trimmedSql);
						String sql = sqlToExecute.toString();
						if(withLog){
							System.out.println("  "+sql);
						}
						executeSQL(sql);
						sqlToExecute.delete(0, sqlToExecute.length());
					} else {
						if (trimmedSql.endsWith(delimiter)) {
							sqlToExecute.append(trimmedSql.substring(0, trimmedSql.length() - delimiter.length()));
							String sql = sqlToExecute.toString();
							if(withLog){
								System.out.println("  "+sql);
							}
							executeSQL(sql);
							sqlToExecute.delete(0, sqlToExecute.length());
						} else {
							sqlToExecute.append(trimmedSql + " ");
						}
					}
				}

			}
		}
	}
}