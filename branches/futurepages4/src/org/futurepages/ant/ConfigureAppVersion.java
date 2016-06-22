package org.futurepages.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.IOException;

public class ConfigureAppVersion extends Task {

	private String classesBaseDir;

	@Override
	public void execute() throws BuildException {
		try {
			Runtime.getRuntime().exec("sed -i \"s/\\!--RELEASE--/param name=\\\"RELEASE\\\" value=\\\"$(date +%Y.%m.%d_%H_%M)\\\"\\//g\" "+ classesBaseDir +"/conf/app-params.xml");
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	public void setClassesBaseDir(String classesBaseDir) {
		this.classesBaseDir = classesBaseDir;
	}
}
