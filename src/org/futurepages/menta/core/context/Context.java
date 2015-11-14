package org.futurepages.menta.core.context;

import java.util.Iterator;


/**
 * Describes the behavior of a Mentawai context.
 * A context has attributes that can be any java object.
 * Each attribute is associated to the context by a name.
 * A context can be invalidated.
 * 
 * @author Sergio Oliveira
 */
public interface Context {
	
	/**
	 * Gets an attribute value associated with the given name.
	 * 
	 * @param name The name of the attribute.
	 * @return The value of the attribute or null if it doesn't exist.
	 */
	public Object getAttribute(String name);
	
	/**
	 * Sets an attribute value associated with the given name.
	 * If the attribute already exists, overwrite it.
	 * 
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * Removes an attribute associated with the given name.
	 * 
	 * @param name The name of the attribute.
	 */
	public void removeAttribute(String name);
	
	/**
	 * Resets this context.
	 * All values are discarded and a new context is internally created.
	 */
	public void reset();
	
	/**
	 * Returns true is an attribute exists with this name.
	 * 
	 * @param name The name of the attribute.
	 * @return true if the attribute exists
	 */
	public boolean hasAttribute(String name);
	
	/**
	 * Return an iterator with all the attribute names in this context.
	 * 
	 * @return an iterator with the attribute names
	 */
	public Iterator<String> keys();
}
