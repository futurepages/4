package org.futurepages.core.config;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.exceptions.AppsPropertiesException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.The;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Parâmetros da aplicação Futurepages
 * 
 * @author leandro
 */
public class Apps {


    public static final  String PARAMS_FILE_NAME                = "futurepages.properties";
	public static final  String MODULES_PATH                    = "modules";
	public static final  String MODULES_PACK                    = "modules";
	public static final  String APPS_PACK                       = "apps";
	public static final  String APPS_PATH                       = "apps";
	public static final  String CONTEXT_CONFIG_DIR_NAME         = "conf";
	public static final  String MODULE_CONFIG_DIR_NAME          = "app_conf";
	public static final  String MODULE_JOBS_SUBPATH             = "control/jobs";
    public static final  String HIBERNATE_ENTITIES_SUBPATH      = "model/entities";
    public static final  String HIBERNATE_ENTITIES_SUBPACK      = "model.entities";
	public static final  String TEMPLATE_PATH                   = "template";
	public static final  String BASE_HIBERNATE_PROPERTIES_FILE  = "hibernate.properties";

	/**
	 * Load application default properties (with no web resource),
	 * seta os especificados no arquivo apps-params.xml
	 */
	private static Apps INSTANCE;

	private HashMap<String, String> paramsMap;

	//cached params:
	private boolean devMode = false;
	private boolean connectExternalModules = false;


	private Apps() throws UnsupportedEncodingException {
		paramsMap = new HashMap<String, String>();
	}

	public static Apps getInstance(){
		return INSTANCE;
	}

	private static synchronized void initStandAlone(){
		try {
			init(null);
		} catch (UnsupportedEncodingException e) {
			AppLogger.getInstance().execute(e);
		}
	}

	public static void init(String contextNameOrClassesPath) throws UnsupportedEncodingException {
		INSTANCE = new Apps();
		String contextName = null;
		String classesRealPath = INSTANCE.defineMainParams(contextNameOrClassesPath);
		if(contextNameOrClassesPath !=null && !contextNameOrClassesPath.contains("/") && !contextNameOrClassesPath.contains("\\")){
			contextName = contextNameOrClassesPath;
		}
		if(contextName!=null){
			getInstance().webDefaultParams(classesRealPath, contextName);
			getInstance().parsePropertiesFile();
			getInstance().cachedParams();
			getInstance().compositeWebParams();
		}else{
			getInstance().parsePropertiesFile();
			getInstance().cachedParams();
			System.out.println(" >> Apps: Only Basic Properties Loaded [NO WEB PROPERTIES].");
		}
	}


	private  void cachedParams() {
		devMode = Apps.get("DEV_MODE").equals("on");
		connectExternalModules = Apps.get("CONNECT_EXTERNAL_MODULES").equals("true");
	}
	
	public static String get(String name) {
		try{
			return getInstance().paramsMap.get(name);
		}catch(NullPointerException e){
			initStandAlone();
			return getInstance().paramsMap.get(name);
		}
	}

	public Map<String, String> getParamsMap(){
		return  paramsMap;
	}

	private String defineMainParams(String classesRoot) throws UnsupportedEncodingException {

		//super-core params
		String classesPath ;
		try{
			classesPath = (new File(FileUtil.classesPath(Apps.class))).getAbsolutePath()+"/";
		}catch (NullPointerException ex){
			classesPath = classesRoot+"/";
		}
		paramsMap.put("CLASSES_PATH", classesPath);

		paramsMap.put("CLASSES_REAL_PATH", classesPath.substring(0, classesPath.length()-1)); //sem a última barra, mantido por conta de legados.
		paramsMap.put("MODULES_CLASSES_REAL_PATH", get("CLASSES_REAL_PATH") + "/" + Apps.MODULES_PATH);
		paramsMap.put("APPS_CLASSES_REAL_PATH", get("CLASSES_REAL_PATH") + "/" + Apps.APPS_PATH);

		//stand-alone params
		paramsMap.put("CONNECT_EXTERNAL_MODULES", "false"); //só quando for dar suporte a mais de um banco de dados
		paramsMap.put("DATABASE_DIR_NAME", "database");
		paramsMap.put("DEPLOY_MODE" , "none");
		paramsMap.put("DEV_MODE" , "off");
    	paramsMap.put("EMAIL_ACTIVE", "false");
		paramsMap.put("EMAIL_DEFAULT_PORT", "25");
		paramsMap.put("EMAIL_SSL_CONNECTION", "false");
		paramsMap.put("INSTALL_MODE", "off");
		paramsMap.put("PAGE_ENCODING", "ISO-8859-1");
		paramsMap.put("MIGRATION_CLASSPATH", "");
		paramsMap.put("SCHEMA_GENERATION_TYPE", "none");
		paramsMap.put("QUARTZ_MODE", "off");
		return classesPath;
	}

	/**
	 * Default Params (WEB Application)
	 */
	private void webDefaultParams(String classesPath, String contextName) {
		//input example: "C:/complete/path/etc/projectName/webSomeStuff/WEB-INF/classes/"
		String applicationRealPath = (new File(classesPath.substring(0, classesPath.length()-16))).getAbsolutePath()+"/"; //16 = "WEB-INF/classes/".length

		contextName = (contextName != null ? contextName : "");

		paramsMap.put("WEB_REAL_PATH", applicationRealPath);
		paramsMap.put("WEBINF_PATH", get("WEB_REAL_PATH") + "WEB-INF");
		paramsMap.put("MODULES_WEB_REAL_PATH", get("WEB_REAL_PATH") + Apps.MODULES_PATH);
		paramsMap.put("CONTEXT_NAME", contextName);

		paramsMap.put("EMAIL_CHARSET", "UTF_8");
//		paramsMap.put("MINIFY_RESOURCE_MODE", "none"); //none, css, js, both //TODO rever esta funcionalidade se precisa continuar
	}


	private String propertiesFilePath = null;
	/**
	 * Parse Futurepages Properties Param File
	 */
	private void parsePropertiesFile() {
		propertiesFilePath = The.concat(paramsMap.get("CLASSES_PATH"), CONTEXT_CONFIG_DIR_NAME, "/", PARAMS_FILE_NAME);

		Properties paramsAndProperties = new Properties();
		InputStreamReader isr = null;

		try {
			isr = new FileReader(propertiesFilePath);
		} catch (FileNotFoundException e) {
			throw new AppsPropertiesException("Futurepages Properties File Not Found: "+ propertiesFilePath,e);
		}
		try {
			paramsAndProperties.load(isr);
		} catch (IOException e) {
			throw new AppsPropertiesException("Unable to find and/or parse Futurepages Properties File: "+ propertiesFilePath,e);
		}
		try {
			isr.close();
		} catch (IOException e) {
			throw new AppsPropertiesException("Unable to find and/or parse Futurepages Properties File: "+ propertiesFilePath,e);
		}
		for(String key : paramsAndProperties.stringPropertyNames()){
			paramsMap.put(key, paramsAndProperties.getProperty(key));
		}
	}

	/**
	 * Constroi os Parâmetros Compostos 
	 */
	private void compositeWebParams() {
		String autoRedirectDomain = get("AUTO_REDIRECT_DOMAIN");
		if(!devMode && autoRedirectDomain!=null 
				&&  (!autoRedirectDomain.startsWith("http://") && !autoRedirectDomain.startsWith("https://"))){
			paramsMap.put("HTTPS_PATH", "https://"+autoRedirectDomain);
		}else{
			paramsMap.put("HTTPS_PATH", "");
		}
	}

	public static boolean devMode(){
		try{
			return getInstance().devMode;

		}catch(NullPointerException e){
			initStandAlone();
			return getInstance().devMode;
		}
	}

	public boolean  connectExternalModules(){
		return connectExternalModules;
	}

	private String regexParam(String key){
		return "^\\s*"+key+"\\=(.*?)$";
	}

	/**
	 * Remove os parâmetros de geração de banco e de compactação de recursos para que
	 * não sejam executados sempre que a aplicação for restartada.
	 * @throws Exception
	 */
	public void removeFileAutomations() throws Exception {
		HashMap<String, String> map = new HashMap();
		map.put(regexParam("MINIFY_RESOURCE_MODE"),"");
		map.put(regexParam("SCHEMA_GENERATION_TYPE"),"");
		map.put(regexParam("INSTALL_MODE"),"");
		map.put(regexParam("MIGRATION_CLASSPATH"),"");
		FileUtil.putStrings(map, propertiesFilePath, propertiesFilePath, true);
	}
}