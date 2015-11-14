package org.futurepages.core.persistence;

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
}