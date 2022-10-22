package org.futurepages.menta.core.consequence;

/**
 * Create a Consequence based on a <b>Convention</b>.
 * 
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * 
 */
public interface ConsequenceProvider {

	/**
	 * Creates a consequence based on a convention, using the name of the
	 * executed action, the result of the action's execution and the executed
	 * inner action.
	 * 
	 * @param action
	 *            the name of the action
	 * @param actionClass
	 * 			  the class representing the action           
	 * @param result
	 *            the result of the action's execution.
	 * @param innerAction
	 *            the inner action called or null if the 'execute()' method was
	 *            called.
	 * @return the Consequence created by convention.
	 */
	public Consequence getConsequence(String action, Class<? extends Object> actionClass, String result, String innerAction);

}