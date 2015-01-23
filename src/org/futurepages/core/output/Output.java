package org.futurepages.core.output;

import java.util.Iterator;

/**
 * Defines the behavior of an action output.
 * An output is like a map where you can put and remove values by name.
 * 
 * @author Sergio Oliveira
 */
public interface Output {
	
	/**
	 * Sets an output value by name.
	 * 
	 * @param name The name of the value
	 * @param value The value
	 */
	public void setValue(String name, Object value);
	
	/**
	 * Gets an output value by name.
	 * 
	 * @param name The name of the value
	 * @return The value or null if it does not exist
	 */
	public Object getValue(String name);
	
	/**
	 * Removes an output value by name.
	 * 
	 * @param name The name of the value
	 */
	public void removeValue(String name);
	
	/**
	 * Returns true is this output has no values.
	 * 
	 * @return true if this output has no values
	 */
	public boolean isEmpty();
	
	/**
	 * Gets an iterator with the names of each output value.
	 * 
	 * @return An iterator with all the names
	 */
	public Iterator<String> keys();
	
	/**
	 * Sets the properties of the given bean in the action output, in other words,
	 * extract all attributes from the given object and place them in the action
	 * output.
	 * 
	 * @param bean The bean (object) from where to get the properties.
	 */
	public void setObject(Object bean);
	
	/**
	 * Sets the properties of the given bean in the action output, in other words,
	 * extract all attributes from the given object and place them in the action
	 * output. Use the given prefix when placing in the output.
	 *  
	 * @param bean The bean (object) from where to get the properties.
	 * @param prefix The prefix to use when placing the properties in the output.
	 */
	public void setObject(Object bean, String prefix);
	
}
	
