package org.futurepages.util;

import org.futurepages.core.config.Apps;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ModuleUtil {

	private File[] modules;
	public static File rootDir;

	private static ModuleUtil INSTANCE = new ModuleUtil();

	public static ModuleUtil getIstance() {
		return INSTANCE;
	}
	
	public File[] getModules() throws UnsupportedEncodingException {

		if(modules == null){
			getClassesPath();
			
			File modulesDir = new File(rootDir + "/" + Apps.MODULES_PATH);
			modules = modulesDir.listFiles();
		}
		return modules;
	}

	public static String getModulePackage(File file) {
		if(isApp(file.getAbsolutePath())){
			return (The.concat(Apps.APPS_PATH,".",getAppName(file.getAbsolutePath())));
		}else{
			return The.concat(Apps.MODULES_PACK,".",file.getName());
		}
	}

	public static boolean isApp(String filePath) {
		return (filePath != null && filePath.replaceAll("\\\\", "/").startsWith(getClassesPath().replaceAll("\\\\", "/") + "apps/"));
	}

	public static String getClassesPath() {
		String classesPath = Apps.get("CLASSES_PATH");
		if (Is.empty(classesPath)) {
			try {
				classesPath = ModuleUtil.class.getResource("/").getPath();
				rootDir = new File(EncodingUtil.correctPath(classesPath));
				return rootDir.getAbsolutePath();
			} catch (UnsupportedEncodingException ignored) {}
		}
		return classesPath;
	}

	public static String getPathByModuleId(String moduleId){
		if(isAppId(moduleId)){
			return The.concat(getClassesPath(),"/",Apps.APPS_PATH,"/",moduleId.replaceAll("_","/"));
		}else{
			return The.concat(getClassesPath(),"/",Apps.MODULES_PACK,"/",moduleId);
		}
	}

	public static boolean isAppId(String moduleId){
		return moduleId.contains("_");
	}

	public static String getPackageByModuleId(String moduleId){
		if(isAppId(moduleId)){
			return The.concat(Apps.APPS_PATH,".",moduleId.replaceAll("_","."));
		}else{
			return The.concat(Apps.MODULES_PACK,".",moduleId);
		}
	}

	public static String moduleId(Class clss){
		return moduleId(clss.getName());
	}

	public static String moduleId(String className){
		if(className.startsWith(Apps.MODULES_PACK+".")){
			return The.firstTokenAfter(className, Apps.MODULES_PACK , ".");
		}else if(className.startsWith(Apps.APPS_PACK+".")){
			List<String> appsList = getAppsList();
			for(String app: appsList){
				if(className.startsWith(app)){
					return app.replaceAll("\\.","_");
				}
			}
		}
		return null;
	}

	public static String moduleId(File file){
		String filePath = file.getAbsolutePath().replaceAll("\\\\","/");
		String modulesPath = Apps.get("MODULES_CLASSES_REAL_PATH").replaceAll("\\\\","/");
		if(filePath.startsWith(modulesPath)){
			return The.firstTokenAfter(filePath, modulesPath, "/");
		}else{
			String appsPath = Apps.get("APPS_CLASSES_REAL_PATH").replaceAll("\\\\","/");
			if(filePath.startsWith(appsPath)){
				List<String> appsList = getAppsList();
				String internalPath = Apps.APPS_PATH+filePath.substring(appsPath.length()).replaceAll("/",".");
				for(String app: appsList){
					if(internalPath.startsWith(app)){
						return app.replaceAll("\\.","_");
					}
				}
			}
		}
		return null;
	}

    /**
     * Registra o Gerenciador dos Módulos (ModuleManager) não comentado
     */
    public static void registerModule(File module) throws Exception {
        if (module.isDirectory()) {
            String moduleName = Apps.MODULES_PACK + "." + module.getName() + ".ModuleManager";
            File moduleManagerFile = new File(Apps.get("MODULES_CLASSES_REAL_PATH") + "/" + module.getName() + "/ModuleManager.class");
            //Registra o Manager do Módulo, caso ele exista.
//            if (moduleManagerFile.exists()) {
//                Class<? extends AbstractApplicationManager> moduleAppManager = (Class<? extends AbstractApplicationManager>) Class.forName(moduleName);
//                manager.register(module.getName(), moduleAppManager);
//            }
        }
    }

    /**
     * Registra os Gerenciadores de todos os Módulos (ModuleManager)
     * não comentados da aplicação
     */
    public static void registerAllModules(File[] modules) throws Exception {
        for (File module : modules) {
            registerModule(module);
        }
    }

    /**
     * Registra somente os Gerenciadores dos Módulos (ModuleManager)
     * que acessam somente a base de dados local
     */
    public static void registerLocalModules(File[] modules) throws Exception {
        for (File module : modules) {
            if (!hasOwnSchema(module)) {
                registerModule(module);
            }
        }
    }

    public static boolean hasOwnSchema(File module) {
        File hiberPropertiesFile = new File(The.concat(module.getAbsolutePath() , "/" , Apps.MODULE_CONFIG_DIR_NAME,"/" , Apps.BASE_HIBERNATE_PROPERTIES_FILE));
		return hiberPropertiesFile.exists();
    }

    public static boolean hasOwnSchema(String moduleId) {
	    return hasOwnSchema(new File(getPathByModuleId(moduleId)));
    }

	public static String getAppName(String dirPath) {
		if(isApp(dirPath)){
			return dirPath.substring((getClassesPath() + Apps.APPS_PATH+"/").length()).replaceAll("[\\\\/]","\\.");
		}
		return null;
	}

	public static File[] listAppsRootDirs(){
		String appsPaths = Apps.get("APPS");
		List<String> listOfApps = new ArrayList();
		String[] apps = appsPaths.split(",");
		for(String app : apps){
			if(!app.contains(":")){
				listOfApps.add(app.trim().replaceAll("\\.", "/"));
			}else{
				listOfApps.add(app.split("\\:")[0].trim().replaceAll("\\.", "/"));
			}
		}
		File[] dirs = new File[listOfApps.size()];
		for(int i = 0; i<dirs.length;i++){
			dirs[i] = new File(getClassesPath()+"/"+listOfApps.get(i));
		}
		return dirs;
	}


	public static List<String> getAppsList(){
		List<String> appsList;
		String appsPaths = Apps.get("APPS");
		appsList = new ArrayList();
		String[] apps = appsPaths.split(",");
		for (String app : apps) {
			if (!app.contains(":")) {
				appsList.add(app.trim());
			} else {
				appsList.add(app.split("\\:")[0].trim());
			}
		}
		return appsList;
	}

	public static File[] listModulesAndApps() {

		File[] apps = listAppsRootDirs();
		File[] modules = (new File(Apps.get("MODULES_CLASSES_REAL_PATH"))).listFiles();
		modules = modules==null? new File[0] : modules;

		File[] modulesAndApps = new File[modules.length+apps.length];
		for(int i = 0; i<modules.length ; i++){
			modulesAndApps[i] = modules[i];
		}
		for(int i = modulesAndApps.length-1; i>=modules.length; i--){
			modulesAndApps[i] = apps[modulesAndApps.length-i-1];
		}
		return modulesAndApps;
	}
}