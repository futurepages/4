package org.futurepages.menta.actions;

import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.AuthenticationFree;

/**
 * Action que não requer autenticação por parte do usuário.
 */
public abstract class FreeAction extends AbstractAction implements AuthenticationFree{

	@Override
    public boolean bypassAuthentication(String innerAction) {
        return true;
    }
}