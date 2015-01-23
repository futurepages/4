package org.futurepages.exceptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ConfigFileNotFoundException extends RuntimeException{

	public ConfigFileNotFoundException(String message) {
		super(message);
	}

	public ConfigFileNotFoundException(UnsupportedEncodingException ex) {
		super(ex);
	}

	public ConfigFileNotFoundException(IOException ex) {
		super(ex);
	}
	
}
