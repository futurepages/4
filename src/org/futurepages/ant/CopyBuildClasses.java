package org.futurepages.ant;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.futurepages.util.SVNUtils;


//TODO - Testar, nunca foi testada.
public class CopyBuildClasses extends Task {

	private String source;
	private String target;
	
	@Override
	public void execute() throws BuildException {
		System.out.println("CopyBuildClasses.execute()");
		System.out.println("source: "+source );
		System.out.println("target: "+target);
	
		try {
			SVNUtils.copy(source, target);
		} catch (IOException e) {
			System.out.println("Falha ao copiar.");
			e.printStackTrace();
		}
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
