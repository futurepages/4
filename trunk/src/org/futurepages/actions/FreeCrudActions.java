package org.futurepages.actions;

/**
 * Actions com suporte a CRUD que não requer autenticação por parte do usuário.
 */
public abstract class FreeCrudActions extends CrudActions {

    @Override
    public boolean bypassAuthentication(String innerAction) {
        return true;
    }
}