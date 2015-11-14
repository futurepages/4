package org.futurepages.menta.core.action;

/**
 * This is an interface to indicate whether an action wants to be blocked by the AuthenticationFilter.
 * 
 * For example, this is the case of the BaseLoginAction.
 * 
 * You can decide this based on the inner action being executed.
 *
 * @author Sergio Oliveira
 */
public interface AuthenticationFree {
   
   public boolean bypassAuthentication(String innerAction);
	
}
