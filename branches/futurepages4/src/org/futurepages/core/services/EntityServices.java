package org.futurepages.core.services;

import org.futurepages.core.persistence.EntityDao;
import org.futurepages.core.validation.EntityValidator;
import org.futurepages.core.validation.Validator;

import java.io.Serializable;
import java.util.HashMap;

public abstract class EntityServices<DAO extends EntityDao<BEAN>,BEAN extends Serializable> {


	protected static HashMap<Class<? extends EntityServices>,EntityServices> INSTANCES = new HashMap();
	protected Class<EntityValidator> validatorClass;
	protected DAO dao;

	public static <BS extends EntityServices, DAO extends EntityDao> BS getInstance(Class<BS> serviceClass, Class<? extends EntityValidator> validatorClass, Class<DAO> entityDaoClass) {
		BS instance = (BS) INSTANCES.get(serviceClass);
		if(instance==null){
			try {
				instance = serviceClass.newInstance();
				instance.validatorClass = validatorClass;
				if(entityDaoClass!=null){
					instance.dao = EntityDao.getInstance(entityDaoClass);
				}
				INSTANCES.put(serviceClass, instance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
	}

	public <V extends Validator> void validate(Class<V> t, Validator.Executor<V> exec) {
		Validator.validate(this,t, exec);
	}

	public BEAN create(BEAN bean){
		//TODO downgrade JDK8 --> JDK6
//		validate(validatorClass, val -> val.create(bean));
//		if(bean instanceof EntityForServices){
//			((EntityForServices)bean).prepareToCreate();
//		}
		return dao.save(bean);
	}

	public BEAN retrieve(Serializable bean){
		return read(dao.getIdValue(bean));
	}


	public BEAN read(Serializable id){
		BEAN bean = dao.get(id);
		//TODO downgrade JDK8 --> JDK6
//		validate(validatorClass, val -> {
//				val.read(bean);
//		});
		if(bean instanceof EntityForServices){
			((EntityForServices)bean).prepareToRead();
		}
		return bean;
	}

	public BEAN update(BEAN bean){
		//TODO downgrade JDK8 --> JDK6
//		validate(validatorClass, val -> val.update(bean));
//		if(bean instanceof EntityForServices){
//			((EntityForServices)bean).prepareToUpdate();
//		}
		return dao.update(bean);
	}

	public BEAN delete(BEAN bean){
		//TODO downgrade JDK8 --> JDK6
//		validate(validatorClass, val -> val.delete(bean));
//		if(bean instanceof EntityForServices){
//			((EntityForServices)bean).prepareToDelete();
//		}
		return dao.delete(bean);
	}

	public DAO dao(){
		return dao;
	}
}