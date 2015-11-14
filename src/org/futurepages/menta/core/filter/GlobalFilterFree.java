package org.futurepages.menta.core.filter;



/**
 * This interface is a market interface that an action can implement to avoid
 * the execution of global filters on itself.
 * 
 * @author Sergio Oliveira
 * @since 1.3
 */
public interface GlobalFilterFree {
 
	public boolean isGlobalFilterFree(String innerAction);
}