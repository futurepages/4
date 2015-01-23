package org.futurepages.core.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.futurepages.util.EnumerationUtil;

/**
 * Encapsulates a ServletContext as a context for Mentawai actions.
 * 
 * @author Sergio Oliveira
 */
public class ApplicationContext implements Context, Map<String, Object> {
	
	private ServletContext context;
	
	/**
	 * Creates a context for this ServletContext.
	 * 
	 * @param context The ServletContext
	 */
	public ApplicationContext(ServletContext context) {
		this.context = context;
	}
	
	public Object getAttribute(String name) {
		return context.getAttribute(name);
	}
	
	public void setAttribute(String name, Object value) {
		context.setAttribute(name, value);
	}
	
	public void removeAttribute(String name) {
		context.removeAttribute(name);
	}
	
    /**
     * Not supported by an ApplicationContext.
     * Calling this method will throw an UnsupportedOperationException!
     */
	public void reset() {
		throw new UnsupportedOperationException("reset() is not supported for ApplicationContext!");
	}
	
	/**
	 * Gets the ServletContext associated with this ApplicationContext.
	 * 
	 * @return The ServletContext associated with this ApplicationContext.
	 */
	public ServletContext getServletContext() {
		return context;
	}
	
	public boolean hasAttribute(String name) {
		return context.getAttribute(name) != null;
	}
	
	public Iterator<String> keys() {
		
		return EnumerationUtil.toIterator(context.getAttributeNames());
		
	}
    
    public void clear() {
    	
    	Iterator<String> iter = keys();
    	
    	while(iter.hasNext()) {
    		
    		removeAttribute(iter.next());
    	}
    }

    /**
     * The implementation is not efficient as it loops through all
     * session values to calculate the size.
     */
    public boolean isEmpty() {
        int size = size();
        return size == 0;
    }

    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return hasAttribute((String) key);
        }
        throw new IllegalArgumentException();
    }

    public boolean containsValue(Object value) {
        
    	Iterator<String> iter = keys();
    	
    	while(iter.hasNext()) {
    		
    		String key = iter.next();
    		
    		Object v = getAttribute(key);
    		
    		if (v != null && v.equals(value)) return true;
    	}
    	
    	return false;
    }

    public Set<Map.Entry<String,Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        if (key instanceof String) {
            return getAttribute((String) key);
        }
        throw new IllegalArgumentException();
    }

    public Set<String> keySet() {
    	
    	int size = size();
    	
    	if (size == 0) return new HashSet<String>(0);
    	
        Set<String> set = new HashSet<String>(size);
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
        	
        	set.add(iter.next());
        }
        
        return set;
    }

    public Object put(String key, Object value)  {
        setAttribute((String) key, value);
        return value;
    }

    public void putAll(Map<? extends String,? extends Object> t) {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
        if (key instanceof String) {
            String s = (String) key;
            Object obj = getAttribute(s);
            if (obj != null) {
                removeAttribute(s);
                return obj;
            }
            return null;
        }
        throw new IllegalArgumentException();
    }

    public int size() {
    	
        int size = 0;
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
        	
        	iter.next();
        	
        	size++;
        	
        }
        
        return size;
    }

    public Collection<Object> values() {
        int size = size();
        
        if (size == 0) return new ArrayList<Object>(0);
        
        List<Object> list = new ArrayList<Object>(size);
        
        Iterator<String> iter = keys();
        
        while(iter.hasNext()) {
        	
        	String key = iter.next();
        	
        	list.add(getAttribute(key));
        }
        
        return list;
    }
}
