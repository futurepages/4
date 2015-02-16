package org.futurepages.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.core.config.Apps;
import org.futurepages.util.FileUtil;
import org.futurepages.util.ModuleUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Classe para a construção da declaração de taglib.
 * @author Danilo
 */
public class BuildWidgetsets extends Task {

	protected static final String BASE_URL = "/META-INF/Widgetsets.gwt.xml";
	private   static final String APP_DEPS_FILE_NAME = "widgetset.dep";
	private   static final String APP_OUTPUT_WIDGETSETS = "/apps/Widgetsets.gwt.xml";
	private   static final String REPLACE_CONSTANT = "<!-- ${INCLUDE_DEPENDENCIES} -->";


	private String classesPath;
	private String source;


	@Override
	public void execute() throws BuildException {
		try {
			Apps.init(classesPath);
			Map<String, String> contentMap = new HashMap<String, String>();

			contentMap.put(REPLACE_CONSTANT, resultOfDeps());

			URL inputFile = getClass().getResource(BASE_URL);
			String outputFilePath = source +APP_OUTPUT_WIDGETSETS;
			FileUtil.putKeyValue(contentMap, inputFile, outputFilePath);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	private String resultOfDeps() throws IOException {
		File[] modulesAndApps = ModuleUtil.listModulesAndApps();
		LinkedHashSet<String> deps = new LinkedHashSet<>();
		for(File f: modulesAndApps){
			File moduleDeps = new File(f.getAbsolutePath()+"/"+Apps.MODULE_CONFIG_DIR_NAME+"/"+APP_DEPS_FILE_NAME);
			if(moduleDeps.exists()){
				String[] depsItems = FileUtil.getStringLines(moduleDeps);
				for(String depItem : depsItems){
					deps.add(depItem);
				}
			}
		}
		StringBuilder resultSB = new StringBuilder();
		for(String dep: deps){
			resultSB.append("\t<inherits name=\"").append(dep).append("\"/>\n");
		}
		return resultSB.toString();
	}

	public void setClassesPath(String classesPath) {
		this.classesPath = classesPath;
	}

	public void setSource(String source) {
		this.source = source;
	}
}