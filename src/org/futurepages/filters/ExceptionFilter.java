package org.futurepages.filters;


import javax.servlet.ServletException;

import org.futurepages.annotations.NotListDependencies;
import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.ErrorException;

public class ExceptionFilter  implements Filter {

	
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
		boolean isErrorException = (cause instanceof ErrorException);
		if(isErrorException){ //Erros causados por Exceptions Esperadas (ErrorExceptions)
			Action action = chain.getAction();
			ErrorException errorException = (ErrorException) cause;
			boolean listDependencies = true;
			if (cause.getClass().isAnnotationPresent(NotListDependencies.class)
			 || chain.getMethod().isAnnotationPresent(NotListDependencies.class)) {
				listDependencies = false;
			}
			return ((AbstractAction) action).putError(listDependencies, errorException);
		}else{
			if(cause instanceof ServletException ){
				throw ((ServletException)cause);  //Erro 500.
			}
			//Exceptions causadas por erros inesperados (muito provavelmente retornará EXCEPTION ou DYN_EXCEPTION)
			return DefaultExceptionLogger.getInstance().execute(cause, chain, chain.getAction().getRequest() , false);
		}
	}
}