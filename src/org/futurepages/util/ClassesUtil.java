package org.futurepages.util;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

public class ClassesUtil {

	private static ClassesUtil instance;

	protected ClassesUtil() {}

	public static ClassesUtil getInstance() {
		if (instance == null) {
			instance = new ClassesUtil();
		}
		return instance;
	}
	
	public <S extends Object> Collection<Class<S>> listClassesFromDirectory(File directory, String srcPath, Class<S> superClass, Class annotation,  boolean deep) {
		ClassFileParser classParser = new ClassFileParser(srcPath, superClass, annotation);
		return new HashSet<Class<S>>(FileUtil.getInstance().listResourcesFromDirectory(directory, classParser, deep));
	}
}