package org.futurepages.util;

import java.io.File;
import java.lang.annotation.Annotation;

import org.apache.commons.lang.StringUtils;
import org.futurepages.core.exception.AppLogger;

class ClassFileParser<K extends Object> extends FileParser<Class<K>> {

	private String classesPath;
	private Class<K> superClass;
	private Class<? extends Annotation> annotation;
	private static ClassFileParser instance;

	private ClassFileParser() {
	}

	public static ClassFileParser getInstance() {
		if (instance == null) {
			instance = new ClassFileParser();
		}
		return instance;
	}

	public ClassFileParser(String classesPath, Class<K> superClass, Class<? extends Annotation> annotation) {
		this.classesPath = (classesPath.charAt(classesPath.length() - 1) != '/' ? classesPath + "/" : classesPath);
		this.superClass = superClass;
		this.annotation = annotation;
	}

	private boolean isValidarSuperClass() {
		return this.superClass != null;
	}

	private boolean isValidarAnnotatedClass() {
		return this.annotation != null;
	}

	private boolean isAnnotatedClass(Class<?> klass) {
		if (isValidarAnnotatedClass()) {
			if (!klass.isAnnotationPresent(annotation)) {
				return false;
			}
		}
		return true;
	}

	private boolean isSubClass(Class<?> klass) {
		if (isValidarSuperClass()) {
			if (!superClass.isAssignableFrom(klass)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Retorna um {@link Class} criado a partir do arquivo passado se o arquivo for uma classe java, 
	 * se for subclasse de {@link #superClass} e for anotada com {@link #annotation} 
	 */
	@Override
	Class<K> parse(File classFile) {
		String classPath = classesPath.replace("\\", "/");
		String absolute = classFile.getAbsolutePath().replace("\\", "/");
		String comparation = StringUtils.difference(classPath, absolute);
		String className = comparation.replace(".class", "").replace("/", ".");
		Class<?> klass = null;

		if (isDotClass(classFile)) {
//			System.out.println("Parsing for Bean Verification: "+classFile.getName());  //for DEBUG-MODE
			try {
				klass = Class.forName(className);
				if (isSubClass(klass) && isAnnotatedClass(klass)) {
					return (Class<K>) klass;
				}
			} catch (ClassNotFoundException e) {
				System.out.println("Não foi possível carregar a classe em " + classFile.getAbsolutePath() + "\n class:" + className);
				AppLogger.getInstance().execute(e);
			}
		}
		return null;
	}

	private boolean isDotClass(File classFile) {
		return classFile.getName().matches(".*\\.class");
	}
}
