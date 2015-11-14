package org.futurepages.menta.exceptions;

import org.futurepages.menta.core.action.Manipulable;

import javax.servlet.ServletException;

/**
 *
 * @author leandro
 */
public class FuturepagesServletException extends ServletException {

	private boolean async = false;

	public FuturepagesServletException(String protocolNum, String actionType, Throwable throwable) {
		super(protocolNum, throwable);
		if (actionType != null) {
			async = actionType.equals(Manipulable.DYN_EXCEPTION);
		}
	}

	public boolean isAsync() {
		return this.async;
	}
}
