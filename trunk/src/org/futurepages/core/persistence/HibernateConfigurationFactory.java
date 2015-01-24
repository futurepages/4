package org.futurepages.core.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.Entity;

import org.futurepages.core.persistence.annotations.View;
import org.futurepages.core.config.Modules;
import org.futurepages.core.config.Params;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.exceptions.ConfigFileNotFoundException;
import org.futurepages.exceptions.ModuleWithoutBeanDirException;
import org.futurepages.util.ClassesUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.Is;

/**
 * Classe de instanciação das Configurações Hibernate; 
 * @author Danilo Medeiros
 */
public class HibernateConfigurationFactory {

	private static HibernateConfigurationFactory INSTANCE;
	private static final String DEFAULT = HibernateManager.DEFAULT;

	public static HibernateConfigurationFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HibernateConfigurationFactory();
		}
		return INSTANCE;
	}

	public File[] getModulesDirs() throws UnsupportedEncodingException {
		File[] modulesDirs = (new File(getRootFileDir() + "/" + Params.MODULES_PATH)).listFiles();
		return modulesDirs;
	}

	public File getRootFileDir() {
		String classPath = this.getClass().getResource("/").getPath();
		try {
			return new File(EncodingUtil.correctPath(classPath));
		} catch (UnsupportedEncodingException ex) {
			System.out.println("erro ao gerar rootDir " + ex.getMessage());
			DefaultExceptionLogger.getInstance().execute(ex);
			return null;
		}
	}

	/**
	 * Retorna o mapa das configurações de cada schema do 'hibernate' na aplicação
	 */
	public Map<String, Configurations> getApplicationConfigurations() throws ConfigFileNotFoundException, UnsupportedEncodingException, FileNotFoundException, IOException {

		Map<String, Schema> schemasMap = new HashMap<String, Schema>();
		File[] modulesDirs = getModulesDirs();
		if (modulesDirs != null) {
			for (File module : modulesDirs) {
				mapModule(module, schemasMap);
			}
		}
		return generateConfigurationsMap(schemasMap); //createMappings & insertProperties

	}

	private void mapModule(File module, Map<String, Schema> schemasMap) throws IOException {
		Properties properties = new Properties();

		String schemaId = getSchemaId(module, properties, schemasMap); //properties é preenchido e devolve schemaId

		if (schemaId != null) { //modulo interno ou externo com CONNECT_EXTERNAL_MODULES=true
			if (schemasMap.get(schemaId) == null) {
				//só vale os properties do primeiro módulo do schema externo. Os properties dos demais
				//servem somente para verificar o schemaId (que foi feito anteriormente em getSchemaId()
				schemasMap.put(schemaId, new Schema());
				schemasMap.get(schemaId).properties = properties;
			}

			Configurations configurations = schemasMap.get(schemaId).config;

			Collection<Class<Object>> classes;
			try {
				classes = listBeansAnnotatedFromModule(module);
				for (Class<?> annotatedClass : classes) {
					configurations.getEntitiesConfig().addAnnotatedClass(annotatedClass);
					if (!annotatedClass.isAnnotationPresent(View.class)) {
						configurations.getTablesConfig().addAnnotatedClass(annotatedClass);
					}
				}
			} catch (ModuleWithoutBeanDirException ex) {
				//Módulo não possui o diretório de beans
				//System.out.println(ex.getMessage());
			}
		} else {
			System.out.println("[ATENÇÃO] Conexão do Módulo Externo Desligado: '"+module.getName()+"'");
		}
	}

	private void insertSchemaProperties(Schema schema) {
		schema.config.getEntitiesConfig().addProperties(schema.properties);
		schema.config.getTablesConfig().addProperties(schema.properties);
		schema.config.getEntitiesConfig().createMappings();
	}

	private Collection<Class<Object>> listBeansAnnotatedFromModule(File module) throws ModuleWithoutBeanDirException {
		File beansDirectory = new File(module.getAbsolutePath() + "/" + Params.BEANS_PACK_NAME);
		if (beansDirectory.listFiles() != null) {
			return ClassesUtil.getInstance().listClassesFromDirectory(beansDirectory, getRootFileDir().getAbsolutePath(), null, Entity.class, true);
		}
		throw new ModuleWithoutBeanDirException(module.getName());
	}

	/**
	 * Devolve o schemaId registrado na propriedade "hibernate.schemaId" e devolve o Properties.
	 * Se "hibernate.schemaId" não for definido, devolve o moduleId.
	 */
	private String getSchemaId(File module, Properties properties, Map<String, Schema> schemasMap) throws FileNotFoundException, IOException {
		boolean defaultModule = !Modules.hasOwnSchema(module);
		if (defaultModule) { //internal
			if(schemasMap.get(DEFAULT)==null){
				schemasMap.put(DEFAULT, new Schema());
				String configPath = "/" + Params.CONFIGURATION_DIR_NAME + "/" + Params.BASE_HIBERNATE_PROPERTIES_FILE;
				String filePath = getRootFileDir().getAbsolutePath() + configPath;
				InputStream inputStream = new FileInputStream(filePath);
				properties.load(inputStream);
				schemasMap.get(DEFAULT).properties = properties;
			}
			return DEFAULT;
		} else if (Params.get("CONNECT_EXTERNAL_MODULES").equals("true")) {
			String configPath = "/" + Params.CONFIGURATION_DIR_NAME + "/" + Params.BASE_HIBERNATE_PROPERTIES_FILE;
			String filePath = module.getAbsolutePath() + configPath;
			InputStream inputStream;
			inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
			String disabled = properties.getProperty("hibernate.disabled");
			if(disabled == null || !disabled.equals("true")){
				String schemaId = properties.getProperty("hibernate.schemaId");
				if (Is.empty(schemaId)) {
					return module.getName(); //se não foi definido, seu schemaId será o nome do módulo.
				}
				return schemaId;
			}
		}
		return null;
	}

	private Map<String, Configurations> generateConfigurationsMap(Map<String, Schema> schemasMap) {
		Map<String, Configurations> configurationsMap = new HashMap<String, Configurations>();
		for(String schemaId : schemasMap.keySet()){
			Schema schema = schemasMap.get(schemaId);
			insertSchemaProperties(schema);
			configurationsMap.put(schemaId, schema.config);
		}
		return configurationsMap;
	}

	class Schema {
		Schema() {
			this.config = new Configurations();
		}
		private Configurations config;
		private Properties properties;
	}
}