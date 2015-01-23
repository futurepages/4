package org.futurepages.core.admin;

import org.futurepages.actions.AjaxAction;
import org.futurepages.actions.DynAction;
import org.futurepages.core.action.Action;
import org.futurepages.core.action.AsynchronousManager;
import org.futurepages.core.action.Manipulable;
import org.futurepages.core.control.InvocationChain;

public class Authentication implements Manipulable {

	@Deprecated
    public static String accessDenied(Action chain) {
    	if (chain instanceof AjaxAction) {
    		return AJAX_DENIED;
		}
    	if (chain instanceof DynAction) {
    		return DYN_DENIED;
    	}
        return ACCESS_DENIED;
    }
	
	public static String accessDenied(InvocationChain chain) {
        if (AsynchronousManager.isAjaxAction(chain)) {
            return AJAX_DENIED;
        }
        if (AsynchronousManager.isDynAction(chain)) {
            return DYN_DENIED;
        }

        return ACCESS_DENIED;
    }

}