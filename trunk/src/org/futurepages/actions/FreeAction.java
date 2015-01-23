package org.futurepages.actions;

import org.futurepages.core.action.AbstractAction;
import org.futurepages.core.admin.AuthenticationFree;

/**
 * Action que não requer autenticação por parte do usuário.
 */
public abstract class FreeAction extends AbstractAction implements AuthenticationFree{

	@Override
    public boolean bypassAuthentication(String innerAction) {
        return true;
    }
}