package org.futurepages.menta.core.callback;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.core.context.Context;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.util.The;

public abstract class ConsequenceCallback implements Runnable {

	protected String actionReturn;
	protected Context actionData;
	protected String caller;

	public void setActionData(Context actionData){
		this.actionData = actionData;
	}

	public void setActionReturn(String actionReturn){
		this.actionReturn = actionReturn;
	}
	public void setCaller(String caller){
		this.caller = caller;
	}

	private void handleException(Exception exCause){
		String message = The.concat("ConsequenceCallback for ", this.caller, "[", this.actionReturn, "] for data ", this.actionData.toString(), " has crashed.");
		ConsequenceCallbackException ex = new ConsequenceCallbackException(message,exCause);
		AppLogger.getInstance().execute(ex);
	}

	private class ConsequenceCallbackException extends Exception {

		public ConsequenceCallbackException(String message,Exception exCause) {
			super(message, exCause);
		}
	}

	@Override
	public final void run() {
		try {
			Thread.sleep(10000); //para dar o tempo necessário para que a ação seja efetivada no banco de dados.
			doRun();
		} catch (Exception ex) {
			handleException(ex);
			if (Dao.getInstance().isTransactionActive()) {
				Dao.getInstance().rollBackTransaction();
			}
		} finally {
			Dao.getInstance().close();
			HibernateManager.getInstance().closeSessions();
		}
	}
	
	public abstract void doRun() throws Exception;
}

