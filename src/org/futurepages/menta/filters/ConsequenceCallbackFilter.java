package org.futurepages.menta.filters;

import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.callback.ConsequenceCallback;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.AfterConsequenceFilter;

/**
 * This filter will place a http cookie value in the action input.
 * 
 * @author Sergio Oliveira Jr.
 */
public class ConsequenceCallbackFilter implements AfterConsequenceFilter {

	private Class<? extends ConsequenceCallback> callbackClass;


   public ConsequenceCallbackFilter(Class<? extends ConsequenceCallback> callbackClass) {
	   this.callbackClass = callbackClass;
   }
	@Override
   public String filter(InvocationChain chain) throws Exception {
     return chain.invoke();     
   }

   public Class<? extends ConsequenceCallback> getCallbackClass(){
		return this.callbackClass;
   }

	@Override
	public void afterConsequence(Action action, Consequence c, boolean conseqExecuted, boolean actionExecuted, String result) {
	}
   
	@Override
   public void destroy() {      
   }
}