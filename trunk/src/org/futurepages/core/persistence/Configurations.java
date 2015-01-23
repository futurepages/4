package org.futurepages.core.persistence;

import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author leandro
 */
public class Configurations {

	private AnnotationConfiguration entitiesConfig;
	private AnnotationConfiguration tablesConfig;

	public Configurations() {
		this.entitiesConfig = new AnnotationConfiguration();
		this.tablesConfig   = new AnnotationConfiguration();
	}

	public AnnotationConfiguration getEntitiesConfig() {
		return entitiesConfig;
	}

	public void setEntitiesConfig(AnnotationConfiguration entitiesConfig) {
		this.entitiesConfig = entitiesConfig;
	}

	public AnnotationConfiguration getTablesConfig() {
		return tablesConfig;
	}

	public void setTablesConfig(AnnotationConfiguration tablesConfig) {
		this.tablesConfig = tablesConfig;
	}

	void createMappings() {
		entitiesConfig.createMappings();
		tablesConfig.createMappings();
	}
}