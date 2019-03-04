package org.futurepages.menta.filters;


import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.exception.ExceptionLogType;
import org.futurepages.core.exception.ExceptionLogger;
import org.futurepages.exceptions.UserException;
import org.futurepages.menta.annotations.NotListDependencies;
import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.action.AsynchronousManager;
import org.futurepages.menta.core.action.Manipulable;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.exceptions.FuturepagesServletException;
import org.futurepages.menta.exceptions.ServletUserException;
import org.futurepages.util.The;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class ExceptionFilter implements Filter {

	
	@Override
	public String filter(InvocationChain chain) throws Exception {
		try {
			return chain.invoke();
		} catch (Throwable throwable) {
			return treatedException(chain, throwable);
		}
	}

	@Override
	public void destroy() {
	}


	/**
	 * 
	 * @return DYN_EXCEPTION ou EXCEPTION para exceptions esperadas, ERROR para exceptions esperadas.
	 */
	public static String treatedException(InvocationChain chain, Throwable ex) throws ServletException {
		Throwable cause = ex;
		if(ex.getCause() != null){
			cause = ex.getCause();
		}
		boolean isErrorException = (cause instanceof UserException);
		if(isErrorException){ //Erros causados por Exceptions Esperadas (ErrorExceptions)
			Action action = chain.getAction();
			UserException errorException = (UserException) cause;
			boolean listDependencies = true;
			if (cause.getClass().isAnnotationPresent(NotListDependencies.class)
			 || chain.getMethod().isAnnotationPresent(NotListDependencies.class)) {
				listDependencies = false;
			}
			return ((AbstractAction) action).putError(listDependencies, errorException);
		}else{
			if(cause instanceof ServletException){
				throw ((ServletException)cause);  //Erro 500.
			}
			//Exceptions causadas por erros inesperados (muito provavelmente retornará EXCEPTION ou DYN_EXCEPTION)
			return Logger.getInstance().execute(cause, chain, chain.getAction().getRequest() , false);
		}
	}

	public static class Logger implements ExceptionLogger, Manipulable {

		private static final Logger INSTANCE = new Logger();

		public static Logger getInstance() {
			return INSTANCE;
		}

		private Logger() {
		}

		public String execute(Throwable throwable) {
			return AppLogger.getInstance().execute(throwable);
		}

		public String execute(Throwable throwable, InvocationChain chain, HttpServletRequest req, boolean status500) throws ServletException {

			ExceptionLogType actionType = null;
			if (chain != null) {
				if (AsynchronousManager.isAsynchronousAction(chain)) {
					actionType = ExceptionLogType.DYN_EXCEPTION;
				} else {
					actionType = ExceptionLogType.EXCEPTION;
				}
			}
			if (status500) {
				Throwable ex;
				if (throwable.getCause() != null) {
					ex = throwable.getCause();
				} else {
					ex = throwable;
				}
				if (ex instanceof ServletUserException) {
					throw (ServletUserException) ex;
				}
				String protocolNumber = AppLogger.getInstance().execute(ex, ExceptionLogType.SERVLET_500, req, null);
				throw new FuturepagesServletException(protocolNumber,(actionType!=null? actionType.name() : null), ex);
			} else if (chain != null) {
				String protocolNumber = AppLogger.getInstance().execute(throwable, actionType , chain.getAction().getRequest() , null);
				chain.getAction().getOutput().setValue(EXCEPTION, new Exception(protocolNumber, throwable));
			}
			return actionType!=null ? The.camelFromSnake(actionType.name()) : null;
		}
	}
}

