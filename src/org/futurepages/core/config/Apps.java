package org.futurepages.core.config;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.exceptions.BadFormedConfigFileException;
import org.futurepages.menta.exceptions.ConfigFileNotFoundException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.The;
import org.futurepages.util.XmlUtil;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parâmetros da aplicação Futurepages
 * 
 * @author leandro
 */
public class Apps {


    public static final String PARAMS_FILE_NAME               = "app-params.xml";
	public static final String MODULES_PATH                   = "modules";
	public static final String MODULES_PACK                   = "modules";
	public static final String APPS_PACK                      = "apps";
	public static final String APPS_PATH                      = "apps";
	public static final String CONFIGURATION_DIR_NAME         = "conf";
    public static final String BEANS_PACK                     = "beans";
    public static final String BEANS_PATH                     = "beans";
	public static final String TEMPLATE_PATH                  = "template";
	public static final String MODULE_JOBS_SUBPATH            = "jobs";
	public static final String BASE_HIBERNATE_PROPERTIES_FILE = "hibernate.properties";


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
		//super-core params
		paramsMap.put("CLASSES_PATH", classesPath);

		paramsMap.put("CLASSES_REAL_PATH", classesPath.substring(0, classesPath.length()-1)); //sem a última barra, mantido por conta de legados.
		paramsMap.put("MODULES_CLASSES_REAL_PATH", get("CLASSES_REAL_PATH") + "/" + Apps.MODULES_PATH);

		paramsMap.put("BEANS_PACK_NAME", "beans");

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
		paramsMap.put("RELEASE", "");
		paramsMap.put("MIGRATION_CLASSPATH", "");
		paramsMap.put("SCHEMA_GENERATION_TYPE", "none");
		paramsMap.put("QUARTZ_MODE", "off");

		// parâmetros de redirecionamento
		paramsMap.put("LOGIN_URL_REDIRECT", null);
		paramsMap.put("LOGIN_URL_REDIRECT_VAR_NAME", "next");
		return classesPath;
	}

	/**
	 * Default App params (WEB Application)
	 */
	private void webDefaultParams(String classesPath, String contextName) {
		//entrada: "C:/path/completo/tal/projectName/webQualquerCoisa/WEB-INF/classes/"
		String applicationRealPath = (new File(classesPath.substring(0, classesPath.length()-16))).getAbsolutePath()+"/"; //16 = "WEB-INF/classes/".length

		contextName = (contextName != null ? contextName : "");

		paramsMap.put("WEB_REAL_PATH", applicationRealPath);
		paramsMap.put("WEBINF_PATH", get("WEB_REAL_PATH") + "WEB-INF");
		paramsMap.put("MODULES_WEB_REAL_PATH", get("WEB_REAL_PATH") + MODULES_PATH);
		paramsMap.put("CONTEXT_NAME", contextName);

		paramsMap.put("DYN_EXCEPTION_FILE_PATH", "/exceptions/dyn/exception.jsp");
		paramsMap.put("EMAIL_CHARSET", "ISO_8859_1");
		paramsMap.put("EXCEPTION_FILE_PATH",  "/exceptions/exception.jsp");
		paramsMap.put("GENERATE_TAGLIB",  "true");
		paramsMap.put("GLOBAL_HEAD_TITLE","");
		paramsMap.put("INIT_ACTION", "init.Index");
		paramsMap.put("INIT_MANAGER_CLASS", "org.futurepages.menta.core.InitManager");
		paramsMap.put("PRETTY_HEAD_TITLE", "");
		paramsMap.put("MINIFY_RESOURCE_MODE", "none"); //none, css, js, both
		paramsMap.put("START_PAGE_NAME", "Index");
        paramsMap.put("THEME", "default");
        paramsMap.put("THEMES_DIR_NAME", "themes");
        paramsMap.put("USE_MODULE_DEPENDENCY", "false"); //control (via ModuleManager)

//for DEBUG-MODE
//		for(String key : paramsMap.keySet()){
//			System.out.println(key+":"+paramsMap.get(key));
//		}
	}


	private String propertiesFilePath = null;
	/**
	 * Parse Futurepages Properties Param File
	 */
	private void parsePropertiesFile() {
		propertiesFilePath = The.concat(paramsMap.get("CLASSES_PATH"), CONFIGURATION_DIR_NAME, "/", PARAMS_FILE_NAME);
		Element appParams;
		try {
			appParams = XmlUtil.getRootElement(propertiesFilePath);
			List<Element> elements = appParams.getChildren();
			for (Element e : elements) {
				String name = e.getAttributeValue("name");
				String value = e.getAttributeValue("value");
				paramsMap.put(name, value);
			}
		} catch (IOException e) {
			throw new ConfigFileNotFoundException("Arquivo de parâmetros da aplicação não encontrado: "+propertiesFilePath);
		} catch (JDOMException e) {
			throw new BadFormedConfigFileException("Arquivo de parâmetros da aplicação mal formado: "+propertiesFilePath);
		}
	}

	/**
	 * Constroi os Parâmetros Compostos 
	 */
	private void compositeWebParams() {
		if (get("START_INDEX") == null) {
            paramsMap.put("START_INDEX", get("START_PAGE_NAME") + ".fpg");
        }

		if (get("RESOURCE_PATH") == null) {
			paramsMap.put("RESOURCE_PATH", TEMPLATE_PATH + "/resource");
        }

		if (get("START_CONSEQUENCE") == null) {
			paramsMap.put("START_CONSEQUENCE", "init/" + get("START_PAGE_NAME") + ".page");
		}
		if (!get("RELEASE").equals("")) {
			paramsMap.put("RELEASE_QUERY", "?release=" + get("RELEASE"));
		} else {
			paramsMap.put("RELEASE_QUERY", "");
		}
		String autoRedirectDomain = get("AUTO_REDIRECT_DOMAIN");
		if(!devMode && autoRedirectDomain!=null
				&&  (!autoRedirectDomain.startsWith("http://") && !autoRedirectDomain.startsWith("https://"))){
			paramsMap.put("HTTPS_PATH", "https://"+autoRedirectDomain);
		}else{
			paramsMap.put("HTTPS_PATH", "");
		}
	}

	private static String regexParam(String key){
		return "<param\\s+[^/]*name\\s*=\\s*\""+key+"\"[^/]*/>";
	}

	public static boolean devMode(){
		try{
			return getInstance().devMode;

		}catch(NullPointerException e){
			initStandAlone();
			return getInstance().devMode;
		}
	}

	public static boolean devLocalMode(){
		try{
			return getInstance().devMode && (get("APP_HOST").startsWith("http://localhost:") || get("APP_HOST").startsWith("http://127.0.0.1:"));

		}catch(NullPointerException e){
			initStandAlone();
			return getInstance().devMode && (get("APP_HOST").startsWith("http://localhost:") || get("APP_HOST").startsWith("http://127.0.0.1:"));
		}
	}

	public static boolean  connectExternalModules(){
		try{
			return getInstance().connectExternalModules;

		}catch(NullPointerException e){
			initStandAlone();
			return getInstance().connectExternalModules;
		}
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