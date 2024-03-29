package org.futurepages.core.persistence;

import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenericDao extends HQLProvider {

	private String schemaId;

	private static GenericDao newInstance(){
		return new GenericDao();
	}

	public static GenericDao newInstance(String schemaId){
		return new GenericDao(schemaId);
	}

	GenericDao(){ this.schemaId = HibernateManager.DEFAULT; }

	GenericDao(String sessionFactoryKey){
		this.schemaId = sessionFactoryKey;
	}

	public GenericDao(Class<EntityDao> entityDaoClass){}

	public Session session() {
		return HibernateManager.getInstance().getSession(schemaId);
	}

	public void clearSession() {
		Session session = session();
		if (session.isDirty()) {
			session.clear();
		}
	}

	public void open() {
		if (!session().isOpen()) {
			session().getSessionFactory().getCurrentSession();
		}
	}

	public boolean isOpen(){
		return session().isOpen();
	}

	public void close() {
		if (session().isOpen()) {
			session().close();
		}
	}

	public <T extends Serializable> void evict(Class<T> entity){
		session().getSessionFactory().getCache().evictEntityRegion(entity);
	}

	public Criteria createCriteria(Class entityClass) {
		return session().createCriteria(entityClass);
	}

	public SQLQuery sqlQuery(String sqlQuery) {
		return session().createSQLQuery(sqlQuery);
	}

	public SQLQuery sqlQuery(String sqlQuery, Class outputClass) {
		return session().createSQLQuery(sqlQuery).addEntity(outputClass);
	}

	public Query selectQuery(HQLQuery hqlQuery) {
		return session().createQuery(hqlQuery.getSelectHQL());
	}

	public Query updateQuery(HQLQuery hqlQuery) {
		return session().createQuery(hqlQuery.getUpdateHQL());
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

	public <T extends Serializable> T sqlToObject(String sql, Class<T> clss) {
		SQLQuery query = sqlQuery(sql);
		query.setResultTransformer(new AliasToBeanResultTransformer(clss));
		return (T) query.uniqueResult();
	}

	public String getIdName(Class entity) {
		ClassMetadata classMetadata = session().getSessionFactory().getClassMetadata(entity);
		if (classMetadata == null) {
			return null;
		}
		return classMetadata.getIdentifierPropertyName();
	}


	public Serializable getIdValue(Object obj) {
		try {
			Field field = obj.getClass().getDeclaredField(getIdName(obj.getClass()));
			field.setAccessible(true);
			return (Serializable) field.get(obj);
		} catch (Exception e) {
			if(obj.getClass().getSuperclass()!=Object.class){
				Field field = null;
				try {
					field = obj.getClass().getSuperclass().getDeclaredField(getIdName(obj.getClass().getSuperclass()));
					field.setAccessible(true);
					return (Serializable) field.get(obj);
				} catch (NoSuchFieldException | IllegalAccessException e2) {
					throw new RuntimeException(e2);
				}
			}
			throw new RuntimeException(e);
		}
	}

	public Class getIdType(Class entity) {
		return session().getSessionFactory().getClassMetadata(entity).getIdentifierType().getClass();

	}

	public boolean isTransactionActive() {
		return session().getTransaction().isActive();
	}


	public Object getMinField(String field, Class entity, String where) {
		return uniqueResult(hql(min(field), entity, where));
	}

	public Object getMaxField(String field, Class entity, String where) {
		return uniqueResult(hql(max(field), entity, where));
	}

	public long getNextLong(String field, Class entity) {
		Long newId = (Long) uniqueResult(hql(max(field) + "+1", entity, null));
		if (newId != null) {
			return newId;
		}
		return 1;
	}

	public Object updateField(Class entity, String field, String expression, String whereClause) {
		updateQuery(hql(field, expression, entity, whereClause)).executeUpdate();
		return selectQuery(hql(field, entity, whereClause)).uniqueResult();
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
		session().createQuery(concat(DELETE, from(entity), as(alias),  where(whereClause))).executeUpdate();
	}

	public long getNextLongId(Class entity) {
		Long newId = (Long) uniqueResult(hql(max(getIdName(entity)) + "+1", entity, null));
		if (newId != null) {
			return newId;
		}
		return (long) 1;
	}

	public int getNextIntId(Class entity) {
		Integer newId = (Integer) uniqueResult(hql(max(getIdName(entity)) + "+1", entity, null));
		if (newId != null) {
			return newId;
		}
		return (int) 1;
	}

	public <T extends Serializable> List<T> list(Class<T> entity) {
		return list(hql(entity,null));
	}

	public <T extends Serializable> List<T> list(HQLQuery<T> hqlQuery) {
		return (List<T>) selectQuery(hqlQuery).list();
	}

	public List listFields(HQLQuery hqlQuery) {
		return (List) selectQuery(hqlQuery).list();
	}

	public <T,R> List<R> list(HQLQuery<T> hqlQuery, Class<R> resultClass) {
		Query query = selectQuery(hqlQuery);
		try {
			query.setResultTransformer(new AliasToBeanConstructorResultTransformer(resultClass.getConstructor(hqlQuery.getEntity())));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		return (List<R>) query.list();
	}

	public <T,R> List<R> topList(int topSize, HQLQuery<T> hqlQuery, Class<R> resultClass) {
		Query query = selectQuery(hqlQuery);
		query.setMaxResults(topSize);
		query.setResultTransformer(new AliasToBeanResultTransformer(resultClass));
		return (List<R>) query.list();

	}

	// pode ser que a lista vem com o array de objetos devido a join com having. O convidado sempre será o primeiro do array.
	public <T extends Serializable> PaginationSlice<T> paginationSlice(Class<T> entityClass, int page, int pageSize, int pagesOffset, HQLQuery<T> hqlQuery) {
		PaginationSlice<T> pagination = new PaginationSlice<T>(page, pageSize, pagesOffset, this, hqlQuery);
		List resultSet = pagination.getList();
		if(resultSet.size()>0){
			if(resultSet.get(0) instanceof Object[]){
				if(!entityClass.isAssignableFrom(((Object[])resultSet.get(0))[0].getClass())){
					throw new IllegalArgumentException("the returned array rows must have the first element as instance of "+entityClass.getName());
				}
				List<T> objs = new ArrayList<>();
				for(Object[] objectsRow : (List<Object[]>) resultSet){
					objs.add((T) objectsRow[0]);
				}
				pagination.setList(objs);
			}
		}
		return pagination;
	}


	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, HQLQuery<T> hql) {
		return paginationSlice(page, pageSize, 0, hql);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, HQLQuery<T> hqlQuery) {
		return new PaginationSlice<T>(page, pageSize, pagesOffset, this, hqlQuery);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSlice(HQLQuery hqlQuery,Class<T> result) {
		return new PaginationSlice(this, hqlQuery,result);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, HQLQuery hql, Class<T> resultClass) {
		return paginationSlice(page, pageSize, 0, hql,resultClass);
	}

	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, int pagesOffset, HQLQuery hqlQuery, Class<T> resultClass) {
		return new PaginationSlice<T>(page, pageSize, pagesOffset, this, hqlQuery,resultClass);
	}



	public <T extends Serializable> List<T> topList(int topSize, HQLQuery<T> hqlQuery) {
		Query query = selectQuery(hqlQuery);
		query.setMaxResults(topSize);
		return query.list();
	}

	public <T extends Serializable> T getFirst(HQLQuery<T> hqlQuery) {
		List<T> objs = topList(1, hqlQuery);
		if(objs.size()>0){
			return objs.get(0);
		}
		return null;
	}

	public <T,R> R getFirst(HQLQuery<T> hqlQuery, Class<R> resultClass) {
		Query query = selectQuery(hqlQuery);
		try {
			query.setResultTransformer(new AliasToBeanConstructorResultTransformer(resultClass.getConstructor(hqlQuery.getEntity())));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		List<R> objs = topList(1, hqlQuery, resultClass);

		if(objs.size()>0){
			return objs.get(0);
		}
		return null;
	}

	public <T extends Serializable> T getLast(Class<T> entityClass) {
		return getFirst(hql(entityClass, null, desc(getIdName(entityClass))));
	}

	public <T extends Serializable> T getLast(HQLQuery<T> hqlQuery) {
		hqlQuery.setOrder(desc((hqlQuery.getAlias()!=null?hqlQuery.getAlias()+".":"")+getIdName(hqlQuery.getEntity())));
		return getFirst(hqlQuery);
	}

	public <T extends Serializable> List<T> listReports(HQLQuery<T> hqlQuery, Class<T> reportClass) {
		Query query = selectQuery(hqlQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return query.list();
	}

	public <T extends Serializable> T getReport(HQLQuery<T> hqlQuery, Class<T> reportClass) {
		Query query = selectQuery(hqlQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return (T) query.uniqueResult();
	}

	public LinkedHashMap map(HQLQuery hqlQuery) {
		Query query = selectQuery(hqlQuery);
		List<Object[]> results = query.list();
		LinkedHashMap map = new LinkedHashMap();
		if(results.size()>0){
			if(results.get(0).length!=2){
				throw new RuntimeException("map needs a select with two columns");
			}
			for (Object[] result : results) {
				map.put(result[0], result[1]);
			}
		}
		return map;
	}

	public <T extends Serializable> T reportTotal(HQLQuery hqlQuery, Class<T> reportClass) {
		Query query = selectQuery(hqlQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		return (T) query.uniqueResult();
	}


	public <T extends Serializable> List<T> topReport(Integer topSize, HQLQuery hqlQuery, Class<T> reportClass) {
		Query query = selectQuery(hqlQuery).setResultTransformer(new AliasToBeanResultTransformer(reportClass));
		if (topSize != null) {
			query.setMaxResults(topSize);
		}
		return query.list();
	}


	public long numRows(HQLQuery hql){
		hql = ReflectionUtil.clone(hql); // clone to avoid problems outside
		hql.setOrder(null);

		String newSelect = count("*");
		if(!Is.empty(hql.getSelect()) && Is.empty(hql.getSelectForSpecialCount())
									  && Is.empty(hql.getGroup())
									  && hql.getSelect().startsWith(DISTINCT)
									  && hql.getSelect().contains(",")){
			newSelect = count(hql.getSelect().split(",")[0]);
		}else{
			if(!Is.empty(hql.getSelect()) || !Is.empty(hql.getSelectForSpecialCount()) || !Is.empty(hql.getGroup())) {
				if (!Is.empty(hql.getSelectForSpecialCount())) {
					newSelect = hql.getSelectForSpecialCount();
				} else if (hql.getSelect().matches("(?i)^\\s*COUNT\\(.*\\)\\s*$")) {
					newSelect = hql.getSelect();
				} else if (Is.empty(hql.getGroup())
						&& !Is.empty(hql.getSelect())
						&& !hql.getSelect().contains(" as ")
						&& !hql.getSelect().contains(" AS ")
						&& !hql.getSelect().contains(",")
						) {
					newSelect = count(hql.getSelect());
				} else if (!Is.empty(hql.getGroup()) && !hql.getGroup().contains(",") && Is.empty(hql.getHaving())) {
					newSelect = count(distinct(hql.getGroup()));
					hql.setGroup(null);
					hql.setHaving(null);
				}
			}
		}
		hql.setSelect(newSelect);

		if(!Is.empty(hql.getSelectForSpecialCount())){
			return selectQuery(hql).list().size();
		}else{
			return (Long) selectQuery(hql).uniqueResult();
		}
	}

	public void beginTransaction() {
		Session session = session();
		if(session.getTransaction().isActive()){
			session.getTransaction().commit();
		}
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
		boolean openAndCloseHere = false;
		if(!isTransactionActive()){
			beginTransaction();
			openAndCloseHere = true;
		}
		session().save(obj);
		if(openAndCloseHere){
			commitTransaction();
		}
		return obj;
	}

	public <T extends Serializable> Collection<T> saveTransaction(Collection<T> objs) {
		boolean openAndCloseHere = false;
		if(!isTransactionActive()){
			beginTransaction();
			openAndCloseHere = true;
		}
		for (T obj : objs) {
			save(obj);
		}
		if(openAndCloseHere){
			commitTransaction();
		}
		return objs;
	}

	public <T extends Serializable> T updateTransaction(T obj) {
		boolean openAndCloseHere = false;
		if(!isTransactionActive()){
			beginTransaction();
			openAndCloseHere = true;
		}
		session().update(obj);
		if(openAndCloseHere){
			commitTransaction();
		}
		return obj;
	}

	public <T extends Serializable> T saveOrUpdateTransaction(T obj) {
		boolean openAndCloseHere = false;
		if(!isTransactionActive()){
			beginTransaction();
			openAndCloseHere = true;
		}
		session().saveOrUpdate(obj);
		if(openAndCloseHere){
			commitTransaction();
		}
		return obj;
	}

	public <T extends Serializable> T deleteTransaction(T obj) {
		boolean openAndCloseHere = false;
		if(!isTransactionActive()){
			beginTransaction();
			openAndCloseHere = true;
		}
		session().delete(obj);
		if(openAndCloseHere){
			commitTransaction();
		}
		return obj;
	}

	public <T extends Serializable> void deleteTransaction(Collection<T> objs) {
		boolean openAndCloseHere = false;
		if(!isTransactionActive()){
			beginTransaction();
			openAndCloseHere = true;
		}
		for (T obj : objs) {
			delete(obj);
		}
		if(openAndCloseHere){
			commitTransaction();
		}
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

//	public <T extends Serializable> T getFromSession(Serializable identifier, Class<T> clazz) {
//		String entityName = clazz.getName();
//		if (identifier == null) {
//			return null;
//		}
//		SessionImplementor sessionImpl = (SessionImplementor) session();
//		EntityPersister entityPersister = sessionImpl.getFactory().getEntityPersister(entityName);
//		PersistenceContext persistenceContext = sessionImpl.getPersistenceContext();
//		EntityKey entityKey = new EntityKey(identifier, entityPersister, EntityMode.POJO.toString());
//		return (T) persistenceContext.getEntity(entityKey);
//	}

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

	public Object get(HQLQuery hql) {
		Query query = selectQuery(hql);
		return query.uniqueResult();
	}

	public <T> T uniqueResult(HQLQuery<T> hqlQuery) {
		Query query = selectQuery(hqlQuery);
		return (T) query.uniqueResult();
	}

	public <T extends Serializable> T load(Class<T> entity, Serializable id) {
		return (T) session().load(entity, id);
	}

	public <T extends Serializable> void evict(T obj) {
		if(isTransactionActive()){
			session().evict(obj);
		}
	}

	public <T extends Serializable> void evict(Collection<T> objs) {
		for (T obj: objs){
			evict(obj);
		}
	}

	//alreadyDeatached is necessary because of possible duplicate references in the object tree. If it happens, it will be detached just the first time.
	private <T extends Serializable> T detachedObject(HashMap<Class<? extends Serializable>,HashMap<Serializable,Serializable>> alreadyDetached, T object, boolean detachCollectionElements){
		if(alreadyDetached.get(object.getClass())!=null){
			Serializable alreadyOne = alreadyDetached.get(object.getClass()).get(getIdValue(object));
			if(alreadyOne!=null){
				return (T) alreadyOne;
			}
		}else{
			alreadyDetached.put(object.getClass(),new HashMap());
		}
		object = get((Class<T>) object.getClass(), getIdValue(object));
		alreadyDetached.get(object.getClass()).put(getIdValue(object),object);

		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object fieldValue = field.get(object);
				if(fieldValue!=null && !field.isAnnotationPresent(Transient.class)){
					if(field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)){
						//System.out.println(field.getName()+" --> "+fieldValue.toString());
						field.set(object,detachedObject(alreadyDetached, (Serializable) field.get(object),detachCollectionElements));
					}
					else //if collection or map...
					if(field.isAnnotationPresent(OneToMany.class)|| field.isAnnotationPresent(ManyToMany.class) || field.isAnnotationPresent(ElementCollection.class)){
						if(fieldValue instanceof Collection){
							if(detachCollectionElements){
								Collection col = ((Collection)fieldValue); //just touch
								if(col.size()>0){
									for(Object obj : col){
										if((obj != null) && obj.getClass().isAnnotationPresent(Entity.class)){
											//System.out.println("ELEMENTS_OF_THE_LIST>" + field.getName() + " --> " + fieldValue.toString());
											field.set(object, detachedObject(alreadyDetached, (Serializable) field.get(obj), false)); //just one level with detached lists. not list inside a object list.
										}
									}
								}
							}else{
								//System.out.println("LIST.SIZE>" + field.getName() + " --> " + fieldValue.toString());
								((Collection)fieldValue).size(); //just touch
							}
						}else if(fieldValue instanceof Map){
							if(detachCollectionElements){
								Map map = ((Map) fieldValue); //just touch
								if (map.size() > 0) {
									for (Object obj : map.values()) {
										if ((obj != null) && obj.getClass().isAnnotationPresent(Entity.class)) {
											//System.out.println("MAP-KEYS-EL.DETACH>" + field.getName() + " --> " + fieldValue.toString());
											field.set(object, detachedObject(alreadyDetached, (Serializable) field.get(obj), false)); //just one level with detached lists. to not have list inside a object list.
										}
									}
									for (Object obj : map.keySet()) {
										if (obj.getClass().isAnnotationPresent(Entity.class)) {
											//System.out.println("MAP-VALUES-EL.DETACH>" + field.getName() + " --> " + fieldValue.toString());
											field.set(object, detachedObject(alreadyDetached, (Serializable) field.get(obj), false)); //just one level with detached lists. to not have list inside a object list.
										}
									}
								}
							}else{
								//System.out.println("MAP-SIZE>" + field.getName() + " --> " + fieldValue.toString());
								((Map)fieldValue).size(); //just touch
							}
						}
					}
				}
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
		return object;
	}

	public <T extends Serializable> T detached(T object, boolean detachCollectionElements){
		HashMap<Class<? extends Serializable>, HashMap<Serializable, Serializable>> alreadyDetached = new HashMap();
		T detachedObject = detachedObject(alreadyDetached, object, detachCollectionElements);
		for(HashMap<Serializable, Serializable> map : alreadyDetached.values()){
			map.values().forEach(this::evict);
		}
		return detachedObject;
	}

	public <T extends Serializable> T detached(T object){
		return detached(object,false);
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

	public List listSQL(String sql) {
		if (!Is.empty(sql)) {
			return sqlQuery(sql).list();
		}else{
			return null;
		}
	}

	public void executeSQLs(String... sqls) {
		executeSQLs(false, sqls);
	}

	//TODO BUG when we have as input like: /* ... [\n]* ... */
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
				int posComment = trimmedSql.indexOf("-- "); //BREAK-ALERT: se possuir essa string dentro de uma variável string, vai quebrar!
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
							sqlToExecute.append(trimmedSql).append(" ");
						}
					}
				}

			}
		}
	}

	public boolean areEqualsById(Object val1, Object val2) {
		try{
			return getIdValue(val1).equals(getIdValue(val2));
		}catch(Exception ignored){
			throw new RuntimeException(ignored);
		}
	}

	public boolean sqlExists(String fromTable, String whereSQL) {
		return Dao.getInstance().getSQL(concat("SELECT EXISTS(SELECT 1 FROM ",fromTable, where(whereSQL),")")).equals(new BigInteger("1"));
	}

	public boolean sqlExists(String fromSql) {
		return Dao.getInstance().getSQL(concat("SELECT EXISTS(SELECT 1 FROM ",fromSql,")")).equals(new BigInteger("1"));
	}

	// pode ser que a lista vem com o array de objetos devido a join com having. O convidado sempre será o primeiro do array.
	public <T extends Serializable> List<T> list(Class<T> entityClass, HQLQuery hql) {
		List resultSet = Dao.getInstance().list(hql);
		if(resultSet.size()>0){
			if(resultSet.get(0) instanceof Object[]){
				if(!entityClass.isAssignableFrom(((Object[])resultSet.get(0))[0].getClass())){
					throw new IllegalArgumentException("the returned array rows must have the first element as instance of "+entityClass.getName());
				}
				List<T> objs = new ArrayList<>();
				for(Object[] objectsRow : (List<Object[]>) resultSet){
					objs.add((T) objectsRow[0]);
				}
				return (List<T>) objs;
			}
		}
		return (List<T>) resultSet;
	}

//	public <T extends Serializable> PaginationSlice<T> paginationSlice(int page, int pageSize, HQLQuery<T> hql) {

		public <T extends Serializable> T getNth(int position, HQLQuery<T> hql) {
		List<T> objs = Dao.getInstance().paginationSlice(position, 1, hql).getList();
		if(objs!=null && objs.size()>0){
			return objs.get(0);
		}
		return null;
	}

	public <T extends Serializable> T getRandom(HQLQuery<T> hql) {
		hql.setOrder("RAND()");
		return Dao.getInstance().getFirst(hql);
	}
}