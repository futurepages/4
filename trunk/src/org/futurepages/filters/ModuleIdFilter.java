package org.futurepages.filters;

import org.futurepages.core.control.AbstractModuleManager;
import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

/**
 * Use este filtro de forma global para gerar no output key = 'actionModuleId'
 * simula qual seria o real módulo daquela consequencia.
 * 
 * o módulo da action
 * @author leandro
 */
public class ModuleIdFilter implements Filter {

    public static final String MODULE_ID_KEY = Action.MODULE_ID_KEY;

	public String correctModuleId;
	public String returnValue;

	public static String getModuleId(Action action) {
		return (String) action.getOutput().getValue(ModuleIdFilter.MODULE_ID_KEY);
	}

	public static void setModuleId(Action action, String moduleId) {
		action.getOutput().setValue(ModuleIdFilter.MODULE_ID_KEY, moduleId);
	}

    public ModuleIdFilter() {}

	public ModuleIdFilter(String correctModuleId) {
			this.correctModuleId = correctModuleId;
		this.returnValue = null;
	}

	public ModuleIdFilter(String returnValue, String correctModuleId) {
		this.correctModuleId = correctModuleId;
		this.returnValue = returnValue;
	}

	@Override
    public String filter(InvocationChain chain) throws Exception {
		//Quando definido localmente para uma consequencia
		if(correctModuleId!=null){
			String chainInvoke =  chain.invoke();
			if((returnValue == null) || (returnValue.equals(chainInvoke))){
					setModuleId(chain.getAction(), correctModuleId);
			}
			return chainInvoke;
		}

		//Quando definido global
		chain.getAction().getOutput().setValue(MODULE_ID_KEY , AbstractModuleManager.moduleId(chain.getAction().getClass()));
        return chain.invoke();
    }

	@Override
    public void destroy() { }
}