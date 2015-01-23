package org.futurepages.core.output;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.futurepages.util.InjectionUtils;

/**
 * A simple Output implementation backed up by a java.util.HashMap.
 * You may use this class as a mock for testing.
 * 
 * @author Sergio Oliveira
 */
public class MapOutput implements Output {
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public MapOutput() { }
	
	public void setValue(String name, Object value) {
		map.put(name, value);
	}
    
	public Object getValue(String name) {
		return map.get(name);
	}
	
	public void removeValue(String name) {
		map.remove(name);
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}
	
	
	public void setObject(Object bean) {
		setObject(bean, null);
	}

	public void setObject(Object bean, String prefix) {
		InjectionUtils.setObject(bean, this, prefix, true);
	}	
	
	public Iterator<String> keys() {
		return map.keySet().iterator();
	}
}
	
