package org.futurepages.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.futurepages.core.config.Apps;
import org.futurepages.core.config.Automations;

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
			return (Automations.getAppName(file.getAbsolutePath()));
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
			List<String> appsList = Apps.getInstance().getAppsList();
			for(String app: appsList){
				if(className.startsWith(app)){
					return app.replaceAll("\\.","_");
				}
			}
		}
		return null;
	}

}