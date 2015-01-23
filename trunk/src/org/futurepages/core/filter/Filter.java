package org.futurepages.core.filter;

import org.futurepages.core.action.Manipulable;
import org.futurepages.core.control.InvocationChain;


/**
 * A filter intercepts an action so it can modify its input and output, before and after the action is executed. 
 * Filters are very useful for validation, authentication, value objects, file upload, etc.
 * 
 * @author Sergio Oliveira
 */
public interface Filter extends Manipulable  {


	/**
	 * Executes the filter.
	 * 
	 * @param chain The InvocationChain for the action this filter is being applied to.
	 * @return The result of the filter or the action the filter is being applied to.
	 */
	public String filter(InvocationChain chain) throws Exception;
    
    /**
     * Gives a chance to the filter to deallocalte any resources before it is destroyed.
     * This is called when the web application is stopped, in other words,
     * this has nothing to do with garbage collection.
     */
    public void destroy();
	
}