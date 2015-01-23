package org.futurepages.actions;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.RedirectAfterLogin;

/**
 * Action que necessita de autenticação para ser acessada.
 *
 * Quando o RedirectFilter está ligado, por padrão o método execute redireciona
 * e as innerActions não. Isso até que se sobrescreva o método shouldRedirect
 * dizendo quais inner serão redirecionadas.
 * 
 * @author leandro
 */
public abstract class ProtectedAction extends AbstractAction implements RedirectAfterLogin{

    public boolean shouldRedirect(String inner) {
        if(inner == null || inner.equals("execute")){
            return true;
        }
        return false;
    }
}
