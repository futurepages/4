package org.futurepages.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.FileUtil;

import java.util.Calendar;
import java.util.HashMap;

public class ConfigureAppVersion extends Task {

	private String classesBaseDir;

	@Override
	public void execute() throws BuildException {
		try {
			HashMap<String,String> contentMap = new HashMap<>();
			contentMap.put("<!--RELEASE-->","<param name=\"RELEASE\" value=\""+ CalendarUtil.format(Calendar.getInstance(), "yyyy-MM-dd_HH_mm_ss")+"\" />");
			String filePath = classesBaseDir+"/conf/app-params.xml";
			FileUtil.putKeyValue(contentMap, filePath, filePath);

		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	public void setClassesBaseDir(String classesBaseDir) {
		this.classesBaseDir = classesBaseDir;
	}
}
