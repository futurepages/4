package org.futurepages.menta.exceptions;

import javax.servlet.ServletException;

public class PageNotFoundException extends ServletException {

	public PageNotFoundException(String msg){
		super(msg);
	}

}
