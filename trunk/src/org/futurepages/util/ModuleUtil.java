package org.futurepages.util;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.futurepages.core.config.Apps;

public class ModuleUtil {

	private File[] modules;
	public static File rootDir;

	private static ModuleUtil INSTANCE = new ModuleUtil();

	public static ModuleUtil getIstance() {
		return INSTANCE;
	}
	
	public static String moduleId(Class klass){
		String className = klass.getName();
		if(!className.startsWith(Apps.MODULES_PACK)){
			return null;
		}
		return The.firstTokenAfter(className, Apps.MODULES_PACK , ".");
	}

	public File[] getModules() throws UnsupportedEncodingException {

		if(modules == null){
			getClassPath();
			
			File modulesDir = new File(rootDir + "/" + Apps.MODULES_PATH);
			modules = modulesDir.listFiles();
		}
		return modules;
	}

	public static String getClassPath() throws UnsupportedEncodingException{
		String classPath = ModuleUtil.class.getResource("/").getPath();
		rootDir = new File(EncodingUtil.correctPath(classPath));
		return rootDir.getAbsolutePath();
	}

}