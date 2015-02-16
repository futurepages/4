package org.futurepages.core.locale;

import com.vaadin.ui.UI;
import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.The;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

/**
 * Parâmetros da aplicação Futurepages
 *
 * @author leandro
 */
public class Txt {

	private HashMap<String, HashMap<String, String>> localesMap = new HashMap<>();

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
			localeId = UI.getCurrent().getLocale().toString();
		}
		String str = getInstance().localesMap.get(localeId).get(txtKey);
		if (str == null) {
			DefaultExceptionLogger.getInstance().execute(new LocaleManagerException("Txt property '" + txtKey + "' not present for locale " + localeId + "."));
			return The.lastTokenOf(txtKey,"\\.").replaceAll("_", " ");
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
						InputStreamReader isr = null;

						isr = new FileReader(localeFile.getAbsolutePath());
						txts.load(isr);
						isr.close();

						HashMap<String,String> localesMap = this.localesMap.get(localeFile.getName());
						if(localesMap==null){
							localesMap = new HashMap<String,String>();
							this.localesMap.put(localeFile.getName(), localesMap );
						}
						String moduleId = ModuleUtil.moduleId(localeFile);
						//System.out.println(moduleId);
						for(String key : txts.stringPropertyNames()){
							String txtValue = txts.getProperty(key);
							if(key.startsWith("$.")){
								key = The.concat(moduleId,".",key.substring(2));
							}
							if(localesMap.get(key)!=null){
								System.out.println(">> Txt key "+key+" overwritten for locale '"+localeFile.toString()+"'. Old Value: '"+localesMap.get(key)+"'; New Value: '"+txtValue+"'");
							}
							localesMap.put(key, txtValue);
							//System.out.println(key+": "+txtValue);
						}
					}
				}
			}
		}
	}
}