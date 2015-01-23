package org.futurepages.core.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * SessionListener registra os sessionListeners dos módulos.
 *
 * Um sessionListener é o resposável por executar atividades na criação
 * e destruição da sessão.
 * 
 * @author leandro
 */
public class SessionListener implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent event) {
		for(SessionEventListener sl : SessionListenerManager.getInstance().getListeners()){
			sl.onCreate(event.getSession());
		}
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		for(SessionEventListener sl : SessionListenerManager.getInstance().getListeners()){
			sl.onDestroy(event.getSession());
		}
	}
}