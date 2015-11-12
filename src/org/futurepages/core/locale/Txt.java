package org.futurepages.core.locale;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.The;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Parâmetros da aplicação Futurepages
 *
 * @author leandro
 */
public class Txt {

	private static Set<String> absentAlreadyInformed = new HashSet();
	private HashMap<String, HashMap<String, String>> localesMap = new HashMap();

	private static Txt INSTANCE;

	public static Txt getInstance() {
		return INSTANCE;
	}

	public Collection<String> getAvailableLocales(){
		return localesMap.keySet();
	}

	private synchronized static void initialize() {
		try {
			initialize(ModuleUtil.listModulesAndApps());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static String get(String txtKey) {
		String localeId = LocaleManager.getDefaultLocale().toString();
		try {
			return doGet(txtKey, localeId);
		} catch (NullPointerException e) {
			initialize();
			return doGet(txtKey, localeId);
		}
	}

	private static String doGet(String txtKey, String localeId) {
		if(localeId==null){
			localeId = null; //TODO era assim ... UI.getCurrent().getLocale().toString();
		}
		if(txtKey.startsWith("$.")){
			Class callerClass = null;
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			for(int i = 1; i<stack.length ; i++ ){
				if(!stack[i].getClassName().equals(Txt.class.getName())){
					try {
						callerClass = Class.forName(stack[i].getClassName());
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
					break;
				}
			}
			String moduleId = ModuleUtil.moduleId(callerClass);
			txtKey = The.concat(moduleId,".",txtKey.substring(2));
		}
		String str = getInstance().localesMap.get(localeId).get(txtKey);
		if (str == null) {
			if(!txtKey.startsWith("[") && txtKey.contains("[") && txtKey.endsWith("]")){
				txtKey = txtKey.replaceAll("\\[.*?\\]","");
				str = getInstance().localesMap.get(localeId).get(txtKey);
			}
			if(str==null && !txtKey.contains("|")){
				if(!absentAlreadyInformed.contains(txtKey)){
					absentAlreadyInformed.add(txtKey);
					AppLogger.getInstance().execute(new TxtNotFoundException(txtKey,localeId));
				}
				String label = The.lastTokenOf(txtKey,"\\.").replaceAll("_", " ");
				return The.capitalizedWords(label);
			}
		}
		return str;
	}

	public static void initialize(File[] appsAndModules) throws IOException {
		INSTANCE = new Txt();
		getInstance().doInit(appsAndModules);
	}

	private void doInit(File[] appsAndModules) throws IOException {
		for(int i = 0; i<appsAndModules.length; i++){
			File localesDir = new File(appsAndModules[i]+"/"+ Apps.MODULE_CONFIG_DIR_NAME+"/locales");
			if(localesDir.exists()){
				File[] localeFiles = localesDir.listFiles();
				if(localeFiles!=null){
					for(File localeFile : localeFiles){
						Properties txts = new Properties();
						InputStreamReader isr =  new FileReader(localeFile.getAbsolutePath());
						txts.load(isr);
						isr.close();

						HashMap<String,String> localesMap = this.localesMap.get(localeFile.getName());
						if(localesMap==null){
							localesMap = new HashMap();
							this.localesMap.put(localeFile.getName(), localesMap );
						}
						String moduleId = ModuleUtil.moduleId(localeFile);

 						for(String key : txts.stringPropertyNames()){
							String txtValue = txts.getProperty(key);
							if(key.startsWith("$.")){
								key = The.concat(moduleId,".",key.substring(2));
							}
							if(localesMap.get(key)!=null){
								System.out.println(">> Txt key "+key+" overwritten for locale '"+localeFile.toString()+"'. Old Value: '"+localesMap.get(key)+"'; New Value: '"+txtValue+"'");
							}
							localesMap.put(key, txtValue);
						}
					}
				}
			}
		}
	}
}