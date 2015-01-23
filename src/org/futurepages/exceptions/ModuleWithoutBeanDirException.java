package org.futurepages.exceptions;

public class ModuleWithoutBeanDirException extends Exception {

	public ModuleWithoutBeanDirException(String moduleName) {
		super("Módulo '"+moduleName+"' não possui diretório de beans");
	}
}
