package org.futurepages.menta.core.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple Context that can be used for a lot of things.
 * 
 * A SessionContext and an ApplicationContext can be mocked with this class.
 *
 * @author Sergio Oliveira
 */
public class MapContext implements Context {
    
    private Map<String, Object> values;
    
    public MapContext() {
        this.values = new HashMap<String, Object>();
    }
    
    public MapContext(Map<String, Object> values) {
        this.values = values;
    }
    
	@Override
	public Object getAttribute(String name) {
        return values.get(name);
    }
	
	@Override
	public void setAttribute(String name, Object value) {
        values.put(name, value);
    }
	
	@Override
	public void removeAttribute(String name) {
        values.remove(name);
    }
	
	@Override
	public Iterator<String> keys() {
		
		return values.keySet().iterator();
	}
	
	@Override
	public void reset() {
        values.clear();
    }
	
	@Override
	public boolean hasAttribute(String name) {
        return values.containsKey(name);
    }

	@Override
	public String toString() {
		return values.toString();
	}
}
