package org.futurepages.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.util.CalendarUtil;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.hibernate.cache.spi.FilterKey;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

public class ConfigureAppVersion extends Task {

	private String baseDir;
	private String env;

	@Override
	public void execute() throws BuildException {
		try {
			if(!Is.empty(env)){
				File filesEnvFrom = new File(baseDir+"/test/env/"+env);
//				System.out.println(filesEnvFrom.getAbsoluteFile());
				File filesEnvTo = new File(baseDir+"/web");
//				System.out.println(filesEnvTo.getAbsoluteFile());
				FileUtil.copy(filesEnvFrom.getAbsolutePath() , filesEnvTo.getAbsolutePath());
			}



			HashMap<String,String> contentMap = new HashMap<>();
			contentMap.put("<!--RELEASE-->","<param name=\"RELEASE\" value=\""+ CalendarUtil.format(CalendarUtil.now(), "yyyy-MM-dd_HH_mm_ss")+"\" />");
			String filePath = baseDir +"/web/WEB-INF/classes/conf/app-params.xml";
			FileUtil.putKeyValue(contentMap, filePath, filePath);


		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public void setEnv(String env) {
		this.env = env;
	}
}
