package org.futurepages.core.persistence;


import org.hibernate.cfg.Configuration;

import java.util.Properties;

/**
 *
 * @author leandro
 */
public class Configurations {

	private Configuration entitiesConfig = new Configuration();
	private Configuration tablesConfig = new Configuration();

	private static HbnFpgNamingStrategy strategyEntities;
	private static HbnFpgNamingStrategy strategyTables;

	public Configurations() {
	}

	public Configuration getEntitiesConfig() {
		return entitiesConfig;
	}

	public void setEntitiesConfig(Configuration entitiesConfig) {
		this.entitiesConfig = entitiesConfig;
	}

	public Configuration getTablesConfig() {
		return tablesConfig;
	}

	public void setTablesConfig(Configuration tablesConfig) {
		this.tablesConfig = tablesConfig;
	}

	void createMappings() {
		entitiesConfig.createMappings();
		tablesConfig.createMappings();
	}

	void addProperties(Properties properties) {
		entitiesConfig.addProperties(properties);
		tablesConfig.addProperties(properties);
	}

	public void setNamingStrategy() {
		entitiesConfig.setNamingStrategy(strategyEntities);
		tablesConfig.setNamingStrategy(strategyTables);
	}

	public static void addClass(Class clazz, boolean entitiesConfig) {
		if(entitiesConfig){
			if(strategyEntities==null){
				strategyEntities = new HbnFpgNamingStrategy();
			}
			strategyEntities.putClass(clazz);
		}else{
			if(strategyTables==null){
				strategyTables = new HbnFpgNamingStrategy();
			}
			strategyEntities.putClass(clazz);
			strategyTables.putClass(clazz);
		}

	}
}