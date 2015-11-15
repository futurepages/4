package org.futurepages.menta.filters;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.persistence.annotations.MultiTransactional;
import org.futurepages.core.persistence.annotations.NonTransactional;
import org.futurepages.core.persistence.annotations.Transactional;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.AfterConsequenceFilter;
import org.hibernate.TransactionException;

import javax.servlet.ServletException;
import java.lang.reflect.Method;

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
				Dao.getInstance().beginTransaction();
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
							Dao.getInstance().commitTransaction();
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
		HibernateManager.getInstance().closeSessions();
	}

	@Override
	public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
		HibernateManager.getInstance().closeSessions();
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
		if (!Apps.connectExternalModules()) {
			Dao.getInstance().beginTransaction();
		}else{
			for (String keySession : HibernateManager.getInstance().getConfigurationsMap().keySet()) {
				Dao.getInstance(keySession).beginTransaction();
			}
		}
	}

	private boolean isTransactionActive(boolean multiTransactional) {
		if (Apps.connectExternalModules() && multiTransactional) {
			for (String keySession : HibernateManager.getInstance().getConfigurationsMap().keySet()) {
				if(Dao.getInstance(keySession).isTransactionActive()){ //presume-se que todas estão, retornará na primeira.
					return true;
				}
			}
			return false;
		}
		return Dao.getInstance().isTransactionActive();
	}

	private void rollbackTransaction(boolean multiTransactional) {
		if (Apps.connectExternalModules() && multiTransactional) {
			for (String keySession : HibernateManager.getInstance().getConfigurationsMap().keySet()) {
				try{
					if(Dao.getInstance(keySession).isTransactionActive()){
						Dao.getInstance(keySession).rollBackTransaction();
					}
				}catch(Exception ex){
					AppLogger.getInstance().execute(new TransactionException("Problem trying to rollback "+keySession+" database",ex));
				}
			}
		}else{
			Dao.getInstance().rollBackTransaction();
		}
	}

	// Commit em todos os bancos externos e no interno. se um deles não funcionar o commit,
	// nenhum fará, quebrará o método para que seja feito o rollback.
	private void commitTransaction() {
		if (Apps.connectExternalModules()) {
				String lastKey = "";
				try{
					for (String keySession : HibernateManager.getInstance().getConfigurationsMap().keySet()) {
						lastKey = keySession;
						Dao.getInstance(keySession).flush();
					}
				}
				catch(Exception ex){
					throw new TransactionException("Problem trying to flush on '"+lastKey+"' database",ex);
				}
				try{
					lastKey = "";
					for (String keySession : HibernateManager.getInstance().getConfigurationsMap().keySet()) {
						lastKey = keySession;
						if(Dao.getInstance(keySession).isTransactionActive()){
							Dao.getInstance(keySession).commitTransaction();
						}
					}
				} catch(Exception ex){
					AppLogger.getInstance().execute(new Exception("Problem trying to commit on '"+lastKey+"' database",ex));
				}
		}else{
			Dao.getInstance().commitTransaction();
		}
	}
}