package org.futurepages.core.persistence;

import org.futurepages.core.pagination.PaginationSlice;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dao extends HQLProvider {

	private static GenericDao INSTANCE;

	static {
		INSTANCE = HibernateManager.getInstance().getDefaultGenericDao();
	}

	public static GenericDao getInstance(String schemaId){
		return HibernateManager.getInstance().getGenericDao(schemaId);
	}

	public static GenericDao getInstance() {
		return INSTANCE;
	}

	public static void clearSession() {
		getInstance().clearSession();
	}

	public static void open() {
		getInstance().open();
	}

	public static void close() {
		getInstance().close();
	}

	public static <T extends Serializable>void evict(Class<T> entity) {
		getInstance().evict(entity);
	}

	public static Criteria createCriteria(Class entityClass) {
		return getInstance().createCriteria(entityClass);
	}

	public static SQLQuery sqlQuery(String sqlQuery) {
		return getInstance().sqlQuery(sqlQuery);
	}

	public static <T extends Serializable> List<T> sqlQueryList(String sqlQuery, Class<T> clss) {
		return getInstance().sqlQueryList(sqlQuery, clss);
	}

	public static <T extends Serializable> List<T> sqlList(String sqlQuery, Class<T> clss) {
		return getInstance().sqlList(sqlQuery, clss);
	}

	public static Query query(String hqlQuery) {
		return getInstance().query(hqlQuery);
	}

	public static String getIdName(Class entity) {
		return getInstance().getIdName(entity);
	}

	public static Class getIdType(Class entity) {
		return getInstance().getIdType(entity);
	}

	public static boolean isTransactionActive() {
		return getInstance().isTransactionActive();
	}

	public static Object uniqueResult(String function, Class entity, String where) {
		return getInstance().uniqueResult(function, entity, where);
	}

	public static Object getMinField(String field, Class entity, String where) {
		return getInstance().getMinField(field, entity, where);
	}

	public static Object getMaxField(String field, Class entity, String where) {
		return getInstance().getMaxField(field, entity, where);
	}

	public static void delete(Class entity, String alias, String whereClause) {
		getInstance().delete(entity, alias, whereClause);
	}

	public static long getNextLong(String field, Class entity) {
		return getInstance().getNextLong(field, entity);
	}

	public static Object updateField(Class entity, String field, String expression, String whereClause) {
		return getInstance().updateField(entity, field, expression, whereClause);
	}

	public static Object incrementField(Class entity, String field, String whereClause, Integer quantidade) {
		return getInstance().incrementField(entity, field, whereClause, quantidade);
	}

	public static Object decrementField(Class entity, String field, String whereClause, Integer quantidade) {
		return getInstance().decrementField(entity, field, whereClause, quantidade);
	}

	public static Object incrementField(Class entity, String field, String whereClause) {
		return getInstance().incrementField(entity, field, whereClause);
	}

	public static Object decrementField(Class entity, String field, String whereClause) {
		return getInstance().decrementField(entity, field, whereClause);
	}

	public static long getNextLongId(Class entity) {
		return getInstance().getNextLongId(entity);
	}

	public static int getNextIntId(Class entity) {
		return getInstance().getNextIntId(entity);
	}

	public static <T extends Serializable> List<T> list(Class<T> entity) {
		return getInstance().list(entity);
	}

	public static <T extends Serializable> List<T> listWithJoin(Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return getInstance().listWithJoin(entity, joinClause, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> listWithJoin(String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return getInstance().listWithJoin(entityAlias, entity, joinClause, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> listDistinctWithJoin(String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return getInstance().listDistinctWithJoin(entityAlias, entity, joinClause, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> listDistinctWithJoin(String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return getInstance().listDistinctWithJoin(entityAlias, fromAndJoin, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> list(String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return getInstance().list(entityAlias, fromAndJoin, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> list(Class<T> entity, String whereClause, String... orderClauses) {
		return getInstance().list(entity, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> list(String hqlQuery) {
		return getInstance().list(hqlQuery);
	}

	public static <T> List<T> list(String hqlQuery, Class<T> resultClass) {
		return getInstance().list(hqlQuery, resultClass);
	}

	public static <T> List<T> topList(int topSize, String hqlQuery, Class<T> resultClass) {
		return getInstance().topList(topSize, hqlQuery, resultClass);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, Class<T> entity, String whereClause, String... orderClauses) {
		return getInstance().paginationSlice(page, pageSize, entity, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, Class<T> entity, String whereClause, String... orderClauses) {
		return getInstance().paginationSlice(page, pageSize, pagesOffset, entity, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return getInstance().paginationSlice(page, pageSize, entityAlias, fromAndJoin, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return getInstance().paginationSlice(page, pageSize, pagesOffset, entityAlias, fromAndJoin, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSliceWithDistinct(int page, int pageSize, String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return getInstance().paginationSliceWithDistinct(page, pageSize, entityAlias, fromAndJoin, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSliceWithDistinct(int page, int pageSize, int pagesOffset, String entityAlias, String fromAndJoin, String whereClause, String... orderClauses) {
		return getInstance().paginationSliceWithDistinct(page, pageSize, pagesOffset, entityAlias, fromAndJoin, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, int pagesOffset, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return getInstance().paginationSliceWithJoin(page, pageSize, pagesOffset, entity, joinClause, whereClause, orderClauses);
	}

	public static <T extends Serializable> PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, int pagesOffset, String entityAlias, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return getInstance().paginationSliceWithJoin(page, pageSize, pagesOffset, entityAlias, entity, joinClause, whereClause, orderClauses);
	}

	public static long numRowsWithDistinct(String entityAlias, String fromClause, String where) {
		return getInstance().numRowsWithDistinct(entityAlias, fromClause, where);
	}

	public static <T extends Serializable> List<T> listPage(int page, int pageSize, int pagesOffset, String hqlQuery) {
		return getInstance().listPage(page, pageSize, pagesOffset, hqlQuery);
	}

	public static <T extends Serializable> List<T> reportPage(int page, int pageSize, int offset, Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		return getInstance().reportPage(page, pageSize, offset, entity, reportClass, fields, where, group, order);
	}


	/* TOP LIST*/
	public static <T extends Serializable> List<T> topListWithJoin(int topSize, Class<T> entity, String joinClause, String whereClause, String... orderClauses) {
		return getInstance().topListWithJoin(topSize, entity, joinClause, whereClause, orderClauses);
	}

	public static <T extends Serializable> List<T> topListWithJoin(int topSize, String entityAlias, Class<T> entity, String joinClause, String where, String... order) {
		return getInstance().topListWithJoin(topSize, entityAlias, entity, joinClause, where, order);
	}

	public static <T extends Serializable> List<T> topList(int topSize, String entityAlias, String fromAndJoin, String where, String... order) {
		return getInstance().topList(topSize, entityAlias, fromAndJoin, where, order);

	}

	public static <T extends Serializable> List<T> topList(int topSize, Class<T> entity, String where, String... order) {
		return getInstance().topList(topSize, entity, where, order);
	}

	public static <T extends Serializable> List<T> topList(int topSize, String hqlQuery) {
		return getInstance().topList(topSize, hqlQuery);
	}

	public static <T extends Serializable> List<T> listReports(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		return getInstance().listReports(entity, reportClass, fields, where, group, order);
	}

	public static <T extends Serializable> T getReport(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		return getInstance().getReport(entity, reportClass, fields, where, group, order);
	}

	public static <T extends Serializable> LinkedHashMap map(Class<T> entity, String key, String value, String where, String... order) {
		return getInstance().map(entity, key, value, where, order);
	}

	public static <T extends Serializable> LinkedHashMap mapGrouped(Class<T> entity, String key, String value, String where, String... order) {
		return getInstance().mapGrouped(entity, key, value, where, order);
	}

	public static <T extends Serializable> Map mapGrouped(Class<T> entity, String entityAlias, String join, String key, String value, String where) {
		return getInstance().mapGrouped(entity, entityAlias, join, key, value, where);
	}

	public static Map mapGrouped(String entityAlias, String fromAndjoin, String key, String value, String where) {
		return getInstance().mapGrouped(entityAlias, fromAndjoin, key, value, where);
	}

	public static Object reportTotal(Class entity, String functions, String where, Class reportClass) {
		return getInstance().reportTotal(entity, functions, where, reportClass);
	}

	public static <T extends Serializable> List<T> topReportWithJoin(int topSize, Class entity,Class<T> reportClass, String fields, String joinClause, String whereClause, String group, String... orderClauses) {
		return getInstance().topReportWithJoin(topSize, entity, reportClass, fields, joinClause, whereClause, group, orderClauses);
	}

	public static <T extends Serializable> List<T> topReportWithJoin(int topSize, String entityAlias, Class entity, Class<T> reportClass, String fields, String joinClause, String whereClause, String group, String... orderClauses) {
		return getInstance().topReportWithJoin(topSize, entityAlias, entity, reportClass, fields, joinClause, whereClause, group, orderClauses);
	}

	public static <T extends Serializable> List<T> topReport(int topSize, String entityAlias, Class entity, Class<T> reportClass, String fields, String fromAndJoin, String whereClause, String group, String... orderClauses) {
		return getInstance().topReport(topSize, entityAlias, entity, reportClass, fields, fromAndJoin, whereClause, group, orderClauses);
	}

	public static <T extends Serializable> List<T> topReport(Integer topSize, Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		return getInstance().topReport(topSize, entity, reportClass, fields, where, group, order);
	}

	public static <T extends Serializable> List<T> report(Class entity, Class<T> reportClass, String fields, String where, String group, String... order) {
		return topReport(null, entity, reportClass, fields, where, group, order);
	}

	public static long numRows(Class entity, String where) {
		Long res = (Long) query(concat(select(count("*")), from(entity), where(where))).uniqueResult();
		return res.longValue();
	}

	public static long numRows(String fromClause, String where) {
		Long res = (Long) query(concat(select(count("*")), fromClause, where(where))).uniqueResult();
		return res.longValue();
	}

	public static long numRows(String whereClause, String group, Class entity) {
		Long res = (Long) query(concat(select(count(distinct(group))), from(entity), where(whereClause))).uniqueResult();
		return res.longValue();
	}

	public static long numRows(String hqlQuery) {
		Long res = (Long) query(hqlQuery).uniqueResult();
		return res.longValue();
	}

	/**
	 * /////////////////////////////////////////////////
	 * MÉTODOS TRANSACIONAIS
	 * /////////////////////////////////////////////////
	 */
	public static void beginTransaction() {
		getInstance().beginTransaction();
	}

	public static void rollBackTransaction() {
		getInstance().rollBackTransaction();
	}

	public static void commitTransaction() {
		getInstance().commitTransaction();
	}

	public static void flush() {
		getInstance().flush();
	}

	public static <T extends Serializable> T saveTransaction(T obj) {
		return getInstance().saveTransaction(obj);
	}

	public static <T extends Serializable> Collection<T> saveTransaction(Collection<T> objs) {
		return getInstance().saveTransaction(objs);

	}

	public static <T extends Serializable> T updateTransaction(T obj) {
		return getInstance().updateTransaction(obj);

	}

	public static <T extends Serializable> T saveOrUpdateTransaction(T obj) {
		return getInstance().saveOrUpdateTransaction(obj);

	}

	public static <T extends Serializable> T deleteTransaction(T obj) {
		return getInstance().deleteTransaction(obj);
	}

	public static <T extends Serializable> void deleteTransaction(Collection<T> objs) {
		getInstance().deleteTransaction(objs);
	}

	public static <T extends Serializable> T save(T obj) {
		return getInstance().save(obj);
	}

	public static <T extends Serializable> void save(T[] arr) {
		getInstance().save(arr);
	}

	public static <T extends Serializable> void save(Collection<T> objs) {
		getInstance().save(objs);
	}

	public static <T extends Serializable> T update(T obj) {
		return getInstance().update(obj);
	}

	public static <T extends Serializable> void update(T[] arr) {
		getInstance().update(arr);
	}

	public static <T extends Serializable> void update(Collection<T> objs) {
		getInstance().update(objs);
	}

	public static <T extends Serializable> T delete(T obj) {
		return getInstance().delete(obj);
	}

	public static <T extends Serializable> void delete(T[] arr) {
		getInstance().delete(arr);
	}

	public static <T extends Serializable> void delete(Collection<T> objs) {
		getInstance().delete(objs);
	}

	public static <T extends Serializable> T saveOrUpdate(T obj) {
		return getInstance().saveOrUpdate(obj);
	}

	public synchronized static <T extends Serializable> T syncSaveOrUpdate(T obj) {
		return getInstance().syncSaveOrUpdate(obj);
	}

	public static <T extends Serializable> T get(Class<T> entity, Serializable id) {
		return getInstance().get(entity, id);
	}

	public static <T> T uniqueResult(Class<T> entity, String where) {
		return getInstance().uniqueResult(entity, where);

	}

	public static <T> T uniqueResult(String hqlQuery) {
		return (T) getInstance().uniqueResult(hqlQuery);
	}

	public static <T extends Serializable> T load(Class<T> entity, Serializable id) {
		return getInstance().load(entity, id);
	}

	public static <T extends Serializable> void evict(T obj) {
		getInstance().evict(obj);
	}

	public static <T extends Serializable> void persist(T obj) {
		getInstance().persist(obj);
	}

	public static <T extends Serializable> T refresh(T obj) {
		return getInstance().refresh(obj);
	}

	public static <T extends Serializable> T merge(T obj) {
		return getInstance().merge(obj);
	}

	public static void executeSQL(String sql) {
		getInstance().executeSQL(sql);
	}

	public static Object getSQL(String sql) {
		return getInstance().getSQL(sql);
	}

	public static void executeSQLs(String... sqls) {
		getInstance().executeSQLs(false,sqls);
	}

	public static void executeSQLs(boolean withLog,String... sqls) {
		getInstance().executeSQLs(withLog, sqls);
	}
}
