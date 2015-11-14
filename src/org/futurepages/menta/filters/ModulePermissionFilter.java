package org.futurepages.menta.filters;

import org.futurepages.core.auth.AllModulesFree;
import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.AuthenticationFree;
import org.futurepages.menta.core.auth.Authentication;
import org.futurepages.menta.core.control.AbstractModuleManager;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;

/**
 * Use este filtro de forma global. Ele verificará em todas actions se trata-se
 * de uma ProtectedAction, se for, verificará em qual pacote se encontra, daí
 * verificará se o usuário logado possui o módulo da Action, só deixará executar
 * qualquer coisa da action se ele possuir o módulo ou for de um tipo de usuário
 * do mesmo módulo.
 * 
 * @author leandro
 */
public class ModulePermissionFilter implements Filter {

    public ModulePermissionFilter() {
    }

	@Override
    public String filter(InvocationChain chain) throws Exception {

		if (chain.getAction() instanceof AbstractAction) {

            AbstractAction action = (AbstractAction) chain.getAction();

            boolean shouldByPass = false;
            if(action instanceof AuthenticationFree){
                AuthenticationFree ac = (AuthenticationFree) action;
                shouldByPass = ac.bypassAuthentication(chain.getInnerAction());
            }
			if(!shouldByPass){
				if(!(action instanceof AllModulesFree)) {
					String moduleId = AbstractModuleManager.moduleId(action.getClass());
					if (action.loggedUser()==null || !action.loggedUser().hasModule(moduleId)) {
							return Authentication.accessDenied(chain);
					}
				}
			}
        }
        return chain.invoke();
    }

   

	@Override
    public void destroy() {
    }
}