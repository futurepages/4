package org.futurepages.core.input;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper for the action input.
 *
 * @author Davi Luan Carneiro
 */
public class InputWrapper implements Input, Map {

	private ThreadLocal<Input> input = new ThreadLocal<Input>();

	public InputWrapper() {

	}

	public InputWrapper(Input input) {
		setInput(input);
	}

	public void setInput(Input input) {
		this.input.set(input);
	}

	private Input getInput() {

		Input i = input.get();

		if (i == null) throw new IllegalStateException("InputWrapper does not have an input!");

		return i;

	}

	private Map<Object, Object> getMap() {

		Input i = getInput();

		if (!(i instanceof Map)) throw new UnsupportedOperationException("Underlying input is not a map!");

		return (Map<Object, Object>) i;

	}

	public String getHeader(String name) {
		return getInput().getHeader(name);
	}

	public String getStringValue(String name) {
		return getInput().getStringValue(name);
	}

	public int getIntValue(String name) {
		return getInput().getIntValue(name);
	}
    
    public int getIntValue(String name, int def) {
        return getInput().getIntValue(name, def);
    }
    
    public boolean hasValue(String name) {
    	
    	return getInput().hasValue(name);
    }
    
    
    public long getLongValue(String name) {
        return getInput().getLongValue(name);
    }
    
    public long getLongValue(String name, long def) {
        return getInput().getLongValue(name, def);
    }
    
    public float getFloatValue(String name) {
        return getInput().getFloatValue(name);
    }
    
    public float getFloatValue(String name, float def) {
        return getInput().getFloatValue(name, def);
    }
    
    public double getDoubleValue(String name) {
        return getInput().getDoubleValue(name);
    }
    
    public double getDoubleValue(String name, double def) {
        return getInput().getDoubleValue(name, def);
    }
    
    public boolean getBooleanValue(String name) {
        return getInput().getBooleanValue(name);
    }

	public Calendar getCalendarValue(String name) {
        return getInput().getCalendarValue(name);
    }
    
    public boolean getBooleanValue(String name, boolean def) {
        return getInput().getBooleanValue(name, def);
    }

	public String[] getStringValues(String name) {
		return getInput().getStringValues(name);
	}

	public int[] getIntValues(String name) {
		return getInput().getIntValues(name);
	}

	public void setValue(String name, Object value) {
		getInput().setValue(name, value);
	}

	public Object getValue(String name) {
		return getInput().getValue(name);
	}

	public void removeValue(String name) {
		getInput().removeValue(name);
	}

	public Iterator<String> keys() {
		return getInput().keys();
	}

	public Iterator<String> getHeaderKeys() {
		return getInput().getHeaderKeys();
	}

	public String getProperty(String name) {
		return getInput().getProperty(name);
	}
	
    public <E> E getObject(Class<? extends E> klass) {
    	
    	return getInput().getObject(klass);
    }
    
    public <E> E getObject(Class<? extends E> klass, String prefix) {
    	
    	return getInput().getObject(klass, prefix);
    }
    
    public <E> E getObject(E bean) {
    	
    	return getInput().getObject(bean);
    }
    
    public <E> E getObject(E bean, String prefix) {
    	
    	return getInput().getObject(bean, prefix);
    }
    
	// MAP METHODS:

    public void clear() {
        getMap().clear();
    }

    public boolean isEmpty() {
        return getMap().isEmpty();
    }

    public boolean containsKey(Object key) {
    	return getMap().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return getMap().containsValue(value);
    }

    public Set entrySet() {
        return getMap().entrySet();
    }

    public Object get(Object key) {
    	return getMap().get(key);
    }

    public Set<Object> keySet() {
        return getMap().keySet();
    }

    public Object put(Object key, Object value)  {
    	return getMap().put(key, value);
    }

    public void putAll(Map map) {
    	getMap().putAll(map);
    }

    public Object remove(Object key) {
    	return getMap().remove(key);
    }

    public int size() {
    	return getMap().size();
    }

    public Collection<Object> values() {
        return getMap().values();
    }

	public Iterator entries() {
        return entrySet().iterator();
	}

	public Date getDate(String name) {
		return getInput().getDate(name);
	}
	
	public Date getDate(String name, String pattern) {
		return getInput().getDate(name, pattern);
	}
	
	public Date getDate(String name, int style) {
		return getInput().getDate(name, style);
	}

	public <E extends Enum<E>> E getEnum(String name, Class<E> enumClass) {
		return getInput().getEnum(name, enumClass);
	}
}
