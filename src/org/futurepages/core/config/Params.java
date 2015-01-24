package org.futurepages.core.config;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.futurepages.core.exception.DefaultExceptionLogger;

import org.jdom.Element;
import org.jdom.JDOMException;

import org.futurepages.exceptions.BadFormedConfigFileException;
import org.futurepages.exceptions.ConfigFileNotFoundException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.The;
import org.futurepages.util.XmlUtil;

/**
 * Parâmetros da aplicação Futurepages
 * 
 * @author leandro
 */
public class Params {

	private static HashMap<String, String> paramsMap;

    public static final  String PARAMS_FILE_NAME       = "app-params.xml";
	public static final  String MODULES_PATH           = "modules";
	public static final  String MODULES_PACK           = "modules";
	public static final  String CONFIGURATION_DIR_NAME = "conf";
    public static final  String BEANS_PACK_NAME        = "beans";
	public static final  String TEMPLATE_PATH          = "template";
	public static final  String BASE_HIBERNATE_PROPERTIES_FILE = "hibernate.properties";

	//cached params:
	private static boolean devMode = false;
	private static boolean connectExternalModules = false;

	/**
	 * Inicializa os parâmetros padrões da aplicação (sem recursos web),
	 * seta os especificados no arquivo app-params.xml
	 */
	static{
		paramsMap = new HashMap<String, String>();
		try {
			System.out.println(">> Params [static]...");
			defineMainParams();
			parseXML();
			System.out.println(">> Params [static] OK");
		} catch (UnsupportedEncodingException ex) {
			System.out.println(">> Params [static] Crashed!");
			DefaultExceptionLogger.getInstance().execute(ex);
		}
	}

	public static void initialize(String contextName) throws UnsupportedEncodingException {
		paramsMap = new HashMap<String, String>();
		String classesPath = defineMainParams();
		webDefaultParams(classesPath, contextName);
		parseXML();
		cachedParams();
		compositeWebParams();
	}


	private static void cachedParams() {
		devMode = Params.get("DEV_MODE").equals("on");
		connectExternalModules = Params.get("CONNECT_EXTERNAL_MODULES").equals("true");
	}
	
	public static String get(String name) {
		return paramsMap.get(name);
	}

	public static Map<String, String> getParamsMap(){
		return  paramsMap;
	}

	private static String defineMainParams() throws UnsupportedEncodingException {

		//super-core params
		String classesPath = (new File(FileUtil.classesPath(Params.class))).getAbsolutePath()+"/";
		paramsMap.put("CLASSES_PATH", classesPath);

		paramsMap.put("CLASSES_REAL_PATH", classesPath.substring(0, classesPath.length()-1)); //sem a última barra, mantido por conta de legados.
		paramsMap.put("MODULES_CLASSES_REAL_PATH", get("CLASSES_REAL_PATH") + "/" + Params.MODULES_PATH);

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
		return classesPath;
	}

	/**
	 * Default Params (WEB Application)
	 */
	private static void webDefaultParams(String classesPath, String contextName) {
		//input example: "C:/complete/path/etc/projectName/webSomeStuff/WEB-INF/classes/"
		String applicationRealPath = (new File(classesPath.substring(0, classesPath.length()-16))).getAbsolutePath()+"/"; //16 = "WEB-INF/classes/".length

		contextName = (contextName != null ? contextName : "");

		paramsMap.put("WEB_REAL_PATH", applicationRealPath);
		paramsMap.put("WEBINF_PATH", get("WEB_REAL_PATH") + "WEB-INF");
		paramsMap.put("MODULES_WEB_REAL_PATH", get("WEB_REAL_PATH") + Params.MODULES_PATH);
		paramsMap.put("CONTEXT_NAME", contextName);

		paramsMap.put("DYN_EXCEPTION_FILE_PATH", "/exceptions/dyn/exception.jsp");
		paramsMap.put("EMAIL_CHARSET", "ISO_8859_1");
		paramsMap.put("EXCEPTION_FILE_PATH",  "/exceptions/exception.jsp");
		paramsMap.put("GENERATE_TAGLIB",  "true");
		paramsMap.put("GLOBAL_HEAD_TITLE","");
		paramsMap.put("INIT_ACTION", "init.Index");
		paramsMap.put("INIT_MANAGER_CLASS", "org.futurepages.core.InitManager");
		paramsMap.put("PRETTY_URL", "false");
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


	private static String pathParamsFile = null;
	/**
	 * Parse xml param file
	 */
	private static void parseXML() {
		pathParamsFile = The.concat(paramsMap.get("CLASSES_PATH"), CONFIGURATION_DIR_NAME , "/", PARAMS_FILE_NAME);
		Element appParams;
		try {
			appParams = XmlUtil.getRootElement(pathParamsFile);
			List<Element> elements = appParams.getChildren();
			for (Element e : elements) {
				String name = e.getAttributeValue("name");
				String value = e.getAttributeValue("value");
				paramsMap.put(name, value);
			}
		} catch (IOException e) {
			throw new ConfigFileNotFoundException("Arquivo de parâmetros da aplicação não encontrado: "+pathParamsFile);
		} catch (JDOMException e) {
			throw new BadFormedConfigFileException("Arquivo de parâmetros da aplicação mal formado: "+pathParamsFile);
		}

	}

	/**
	 * Constroi os Parâmetros Compostos 
	 */
	private static void compositeWebParams() {

		if (get("START_INDEX") == null) {
            paramsMap.put("START_INDEX", get("START_PAGE_NAME") + ".fpg");
        }

		if (get("RESOURCE_PATH") == null) {
			paramsMap.put("RESOURCE_PATH", TEMPLATE_PATH + "/resource");
        }

		if (get("START_CONSEQUENCE") == null) {
			paramsMap.put("START_CONSEQUENCE", "init/" + Params.get("START_PAGE_NAME") + ".page");
		}
		if (!get("RELEASE").equals("")) {
			paramsMap.put("RELEASE_QUERY", "?release=" + Params.get("RELEASE"));
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

	public static boolean devMode(){
		return devMode;
	}

	public static boolean connectExternalModules(){
		return connectExternalModules;
	}

	private static String regexParam(String key){
		return "<param\\s+[^/]*name\\s*=\\s*\""+key+"\"[^/]*/>";
	}

	/**
	 * Remove os parâmetros de geração de banco e de compactação de recursos para que
	 * não sejam executados sempre que a aplicação for restartada.
	 * @throws Exception
	 */
	public static void removeFileAutomations() throws Exception {
		HashMap<String, String> map = new HashMap();
		map.put(regexParam("MINIFY_RESOURCE_MODE"),"");
		map.put(regexParam("SCHEMA_GENERATION_TYPE"),"");
		map.put(regexParam("INSTALL_MODE"),"");
		map.put(regexParam("MIGRATION_CLASSPATH"),"");
		FileUtil.putStrings(map, pathParamsFile, pathParamsFile, true);
	}
}