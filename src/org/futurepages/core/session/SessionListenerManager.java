package org.futurepages.core.session;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.futurepages.core.tags.build.ModulesAutomation;
import org.futurepages.exceptions.NotModuleException;

/**
 * Gerencia o SessionListener
 *
 * @author leandro
 */
public class SessionListenerManager extends ModulesAutomation{

	private static final String LISTENERS_DIR_NAME = "listeners";

    private final List<SessionEventListener> listeners;

    private static SessionListenerManager instance;

	public SessionListenerManager(File[] modules){
			super(modules, LISTENERS_DIR_NAME);
			listeners = new ArrayList<SessionEventListener>();
			instance = this;
	}
	
	public static SessionListenerManager getInstance() {
		return instance;
	}
    
	public List<SessionEventListener> getListeners(){
		return listeners;
	}

    public void initialize() throws InstantiationException, IllegalAccessException, NotModuleException{
			Map<String, List<Class<SessionEventListener>>> classes = getModulesDirectoryClasses(SessionEventListener.class, null);

			for (String moduleName : classes.keySet()) {
				for (Class<SessionEventListener> listener : classes.get(moduleName)) {
					log(moduleName+" : "+listener.getName());
					listeners.add(listener.newInstance());

				}
			}
    }

    private static void log(String msg) {
        System.out.println("[:sessionListener:] " + msg);
    }
}