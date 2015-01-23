package org.futurepages.filters;

import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

/**
 * Filtro verifica se um determinado atributo da sessão existe.
 * Caso não exista, ele é redirecionado para um determinado retorno.
 */
public class NoSessionRedirectFilter implements Filter {

    private String sessionAttribute;
    private String noSessionReturn;

    public NoSessionRedirectFilter(String sessionAttribute, String returnsTo) {
        this.sessionAttribute = sessionAttribute;
        this.noSessionReturn = returnsTo;
    }

    public String filter(InvocationChain chain) throws Exception {
        if(!chain.getAction().getSession().hasAttribute(sessionAttribute)){
            return noSessionReturn;
        }
        else {
            return chain.invoke();
		}
    }

    public void destroy() {
    }
}