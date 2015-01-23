package org.futurepages.core.persistence;

import java.lang.reflect.Method;
import javax.servlet.ServletException;
import org.futurepages.annotations.MultiTransactional;

import org.futurepages.annotations.NonTransactional;
import org.futurepages.annotations.Transactional;
import org.futurepages.core.action.Action;
import org.futurepages.core.config.Params;
import org.futurepages.core.consequence.Consequence;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.filter.AfterConsequenceFilter;
import org.futurepages.filters.ExceptionFilter;

public class HibernateFilter implements AfterConsequenceFilter {

	public HibernateFilter() {
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {

		boolean hasError = true;
		boolean isTransactional = isTransactional(chain);
		boolean isMultiTransactional = isMultiTransactional(chain);

		try {
			if (isTransactional) {
				Dao.beginTransaction();
			}else if(isMultiTransactional){
				beginMultiTransaction();
			}
			String result = chain.invoke();
			if(!result.equals(ERROR) && !result.equals(AJAX_ERROR)){
				hasError = false;
			}
			return result;
			
		} catch (Throwable throwable) {
			return ExceptionFilter.treatedException(chain, throwable);
		} finally {
			if (isTransactionActive(isMultiTransactional)) {
				if (hasError) {
					rollbackTransaction(isMultiTransactional);
				} else {
					try{
						if (isTransactional) {
							Dao.commitTransaction();
						}else if(isMultiTransactional){
							commitTransaction();
						}
					}catch(Exception ex){
						rollbackTransaction(isMultiTransactional);
						return ExceptionFilter.treatedException(chain, ex);
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		HibernateManager.closeSessions();
	}

	@Override
	public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
		HibernateManager.closeSessions();
	}

	/**
	 * Define se a action é transacional ou não.
	 * @return
	 * false: se o método estiver anotado com {@link NonTransactional}. 
	 * <br>true: se a Classe ou o método estiverem anotados com {@link Transactional} o métoro retornará true.
	 * <br>false: se o método estiver anotado com {@link Transactional} e {@link NonTransactional} simultaneamente
	 */
	protected boolean isTransactional(InvocationChain chain) throws ServletException {
		boolean result;
		Method method = chain.getMethod();
		if(method == null){
			throw new ServletException("Inner action '"+chain.getInnerAction()+"' for action '"+chain.getActionName()+"' not found.");
		}
		boolean metodoNaoTransacional = method.isAnnotationPresent(NonTransactional.class);
		if(metodoNaoTransacional){
			result = false;
		}else{
			Class<?> klass = chain.getAction().getClass();
			boolean classeTransacional = klass.isAnnotationPresent(Transactional.class);
			boolean metodoTransacional = method.isAnnotationPresent(Transactional.class);
			result = metodoTransacional || classeTransacional;
		}
		
		return result;
	}

	private boolean isMultiTransactional(InvocationChain chain) throws ServletException {
		boolean result;
		Method method = chain.getMethod();
		if(method == null){
			throw new ServletException("Inner action '"+chain.getInnerAction()+"' for action '"+chain.getActionName()+"' not found.");
		}
		boolean metodoNaoTransacional = method.isAnnotationPresent(NonTransactional.class);
		if(metodoNaoTransacional){
			result = false;
		}else{
			Class<?> klass = chain.getAction().getClass();
			boolean classeTransacional = klass.isAnnotationPresent(MultiTransactional.class);
			boolean metodoTransacional = method.isAnnotationPresent(MultiTransactional.class);
			result = metodoTransacional || classeTransacional;
		}
		
		return result;
	}

	private void beginMultiTransaction() {
		if (!Params.connectExternalModules()) {
			Dao.beginTransaction();
		}else{
			for (String keySession : HibernateManager.getConfigurationsMap().keySet()) {
					Dao.getInstance(keySession).beginTransaction();
			}
		}
	}

	private boolean isTransactionActive(boolean multiTransactional) {
		if (Params.connectExternalModules() && multiTransactional) {
			for (String keySession : HibernateManager.getConfigurationsMap().keySet()) {
				if(Dao.getInstance(keySession).isTransactionActive()){ //presume-se que todas estão, retornará na primeira.
					return true;
				}
			}
			return false;
		}
		return Dao.isTransactionActive();
	}

	private void rollbackTransaction(boolean multiTransactional) {
		if (Params.connectExternalModules() && multiTransactional) {
			for (String keySession : HibernateManager.getConfigurationsMap().keySet()) {
				try{
					Dao.getInstance(keySession).rollBackTransaction();
				}catch(Exception ex){
					DefaultExceptionLogger.getInstance().execute(ex);
				}
			}
		}else{
			Dao.rollBackTransaction();
		}
	}

	// Commit em todos os bancos externos e no interno. se um deles não funcionar o commit,
	// nenhum fará, quebrará o método para que seja feito o rollback.
	private void commitTransaction() {
		if (Params.connectExternalModules()) {
			for (String keySession : HibernateManager.getConfigurationsMap().keySet()) {
				Dao.getInstance(keySession).flush();
			}
			for (String keySession : HibernateManager.getConfigurationsMap().keySet()) {
				Dao.getInstance(keySession).commitTransaction();
			}
		}else{
			Dao.commitTransaction();
		}
	}
}