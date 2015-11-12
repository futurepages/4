package org.futurepages.core.persistence;

import org.futurepages.util.ModuleUtil;

import java.io.Serializable;
import java.util.HashMap;

public abstract class EntityDao<BEAN extends Serializable> extends GenericDao {

	public abstract Class<BEAN> getEntityClass();
	protected static HashMap<Class<? extends EntityDao>,EntityDao> INSTANCES = new HashMap();
	protected GenericDao dao;

	public static <ED extends EntityDao> ED getInstance(Class<ED> edClass){
		ED instance = (ED) INSTANCES.get(edClass);
		if(instance==null){
			try {
				instance = edClass.newInstance();
				String moduleId = ModuleUtil.moduleId(edClass);
				if(!ModuleUtil.hasOwnSchema(moduleId)){
					instance.dao = Dao.getInstance();
				}else{
					instance.dao = Dao.getInstance(moduleId);
				}
				INSTANCES.put(edClass, instance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
	}

	public BEAN get(Serializable id){
		return super.get(getEntityClass(), id);
	}

	public GenericDao dao(){
		return dao;
	}
}
