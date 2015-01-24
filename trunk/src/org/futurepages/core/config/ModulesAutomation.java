package org.futurepages.core.config;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.futurepages.core.config.Params;
import org.futurepages.exceptions.NotModuleException;
import org.futurepages.util.ClassesUtil;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.ModuleUtil;

public abstract class ModulesAutomation {

	protected File[] modules;
	private String dirName;
	private List applicationClasses = null;

	public ModulesAutomation(File[] modules, String dirName) {
		super();
		this.modules = modules;
		this.dirName = dirName;
	}

	public File[] getModules() {
		return modules;
	}

	public String getDirName() {
		return dirName;
	}

	protected <S extends Object> List<Class<S>> getApplicationClasses(Class<S> superKlass, Class<? extends Annotation> annotation) {
		
		File dirr = new File(getClassPath() + this.getDirName());
		return getClasses(dirr, superKlass, annotation);
	}

	protected <S extends Object> List<Class<S>> getClasses(File dirr, Class<S> superKlass, Class<? extends Annotation> annotation) {
		if (applicationClasses == null) {
			applicationClasses = new ArrayList<Class<S>>(ClassesUtil.getInstance().listClassesFromDirectory(
					dirr, getClassPath(), superKlass, annotation, true));
		}
		return applicationClasses;
	}

	public <S extends Object> Map<String, List<Class<S>>> getModulesDirectoryClasses(Class<S> superKlass, Class<? extends Annotation> annotation) throws NotModuleException {

		Map<String, List<Class<S>>> modulesClasses = new HashMap<String, List<Class<S>>>();
		List<Class<S>> classes;
		if (this.modules != null) {
			for (File module : this.modules) {

				if (module.isDirectory()) {

					final File dir = getSubFile(module, getDirName());
					classes = new ArrayList<Class<S>>(ClassesUtil.getInstance().listClassesFromDirectory(
							dir, getClassPath(), superKlass, annotation, true));

					sortClassList(classes);
					modulesClasses.put(module.getName(), classes);
				}
				else{
					throw new NotModuleException(module.getAbsolutePath()+" não pode ficar dentro da pasta de módulos");
				}
			}
		}
		return modulesClasses;
	}

	private String getClassPath() {
		String classPath = Params.get("CLASSES_PATH");
		if(Is.empty(classPath)){
			try {
				classPath = ModuleUtil.getClassPath();
			} catch (UnsupportedEncodingException e) {
			}
		}
		return classPath;
	}

	private <S extends Object> void sortClassList(List<Class<S>> classes) {
		Collections.sort(classes, new Comparator<Class<S>>() {

			@Override
			public int compare(Class<S> o1, Class<S> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	protected File getSubFile(File file, String name) {
		return FileUtil.getInstance().getSubFile(file, name);
	}
}

