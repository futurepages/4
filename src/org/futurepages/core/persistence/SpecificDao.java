package org.futurepages.core.persistence;

import org.futurepages.core.pagination.PaginationSlice;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class SpecificDao<T extends Serializable> extends GenericDao {

	Class<T> entityClass;

	protected static SpecificDao INSTANCE = null;

	@SuppressWarnings("unchecked")
	public static  <SD extends SpecificDao, T> SD getInstance(Class<SD> classInstance, Class<T> entityClass) {
		if(INSTANCE==null){
			try {
				INSTANCE = classInstance.newInstance();
				INSTANCE.entityClass = entityClass;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (SD) INSTANCE;
	}


	public Criteria createCriteria() {
		return super.createCriteria(entityClass);
	}


	public SQLQuery sqlQuery(String sqlQuery) {
//		System.out.println("SQL: "+sqlQuery); //para depurar SQls, descomente. // for DEBUG-MODE.
		return super.sqlQuery(sqlQuery, entityClass);
	}

	public List<T> sqlQueryList(String sql) {
		return super.sqlQueryList(sql, entityClass);
	}

	public List<T> sqlList(String sql) {
		return super.sqlList(sql, entityClass);
	}

	public String getIdName() {
		return super.getIdName(entityClass);
	}

	public Class getIdType() {
		return super.getIdType(entityClass);
	}

	public Object uniqueResult(String function, String where) {
		return super.uniqueResult(function, entityClass, where);
	}

	public Object getMinField(String field, String where) {
		return super.getMinField(field, entityClass, where);
	}

	public Object getMaxField(String field, String where) {
		return super.getMaxField(field, entityClass, where);
	}

	public long getNextLong(String field) {
		return super.getNextLong(field, entityClass);
	}

	public Object updateField(String field, String expression, String whereClause) {
		return super.updateField(entityClass, field, expression, whereClause);
	}

	public Object incrementField(String field, String whereClause, Integer quantidade) {
		return super.incrementField(entityClass, field, whereClause, quantidade);
	}

	public Object decrementField(String field, String whereClause, Integer quantidade) {
		return super.decrementField(entityClass, field, whereClause, quantidade);
	}

	public Object incrementField(String field, String whereClause) {
		return super.incrementField(entityClass, field, whereClause);
	}

	public Object decrementField(String field, String whereClause) {
		return super.decrementField(entityClass, field, whereClause);
	}

	public void delete(String alias, String whereClause) {
		super.delete(entityClass, alias, whereClause);
	}

	public long getNextLongId() {
		return super.getNextLongId(entityClass);
	}

	public int getNextIntId() {
		return super.getNextIntId(entityClass);
	}

	public List<T> list() {
		return super.list(entityClass);
	}

	public List<T> listWithJoin(String joinClause, String whereClause, String... orderClauses) {
		return super.listWithJoin(entityClass, joinClause, whereClause, orderClauses);
	}

	public List<T> listWithJoin(String entityAlias, String joinClause, String whereClause, String... orderClauses) {
		return super.listWithJoin(entityAlias,entityClass,joinClause,whereClause,orderClauses);
	}

	public List<T> listDistinctWithJoin(String entityAlias, String joinClause, String whereClause, String... orderClauses) {
		return super.listDistinctWithJoin(entityAlias,entityClass,joinClause,whereClause,orderClauses);
	}

	public List<T> list(String whereClause, String... orderClauses) {
		return super.list(entityClass, whereClause, orderClauses);
	}

	public PaginationSlice<T> paginationSlice(int page, int pageSize, String whereClause, String... orderClauses) {
		return super.paginationSlice(page,pageSize, entityClass, whereClause, orderClauses);
	}
	public PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, String whereClause, String... orderClauses) {
		return super.paginationSlice(page, pageSize,pagesOffset, entityClass, whereClause, orderClauses);
	}

	public PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, int pagesOffset, String joinClause, String whereClause, String... orderClauses) {
		return super.paginationSliceWithJoin(page,pageSize,pagesOffset,entityClass,joinClause,whereClause,orderClauses);
	}

	public PaginationSlice<T> paginationSliceWithJoin(int page, int pageSize, int pagesOffset, String entityAlias, String joinClause, String whereClause, String... orderClauses) {
		return super.paginationSliceWithJoin(page,pageSize,pagesOffset, entityAlias,entityClass,joinClause,whereClause,orderClauses);
	}


	public <X extends Serializable> List<X> reportPage(int page, int pageSize, int offset, Class<X> reportClass, String fields, String where, String group, String... order) {
		return super.reportPage(page, pageSize, offset, entityClass, reportClass, fields, where, group, order);
	}

	public List<T> topListWithJoin(int topSize, String joinClause, String whereClause, String... orderClauses) {
		return super.topListWithJoin(topSize,entityClass,joinClause, whereClause, orderClauses);
	}

	public List<T> topListWithJoin(int topSize, String entityAlias, String joinClause, String where, String... order) {
		return super.topListWithJoin(topSize,entityAlias,entityClass,joinClause, where, order);
	}

	public List<T> topList(int topSize, String where, String... order) {
		return super.topList(topSize,entityClass,where,order);
	}

	public T getReport(Class<T> reportClass, String fields, String where, String group, String... order) {
		return super.getReport(entityClass, reportClass, fields, where, group, order);
	}

	public LinkedHashMap map(String key, String value, String where, String... order) {
		return super.map(entityClass, key, value, where, order);
	}

	public LinkedHashMap mapGrouped(String key, String value, String where, String... order) {
		return mapGrouped(entityClass,key,value,where, order);
	}

	public Map mapGrouped(String entityAlias, String join, String key, String value, String where) {
		return mapGrouped(entityClass, join, key, value, where);
	}

	public Object reportTotal(String functions, String where, Class reportClass) {
		return super.reportTotal(entityClass,functions,where,reportClass);
	}

	public long numRows(String where) {
		return super.numRows(entityClass,where);
	}

	public T get(Serializable id) {
		return super.get(entityClass,id);
	}

	public T uniqueResultWhere(String where) {
		return super.uniqueResult(entityClass, where);
	}
}