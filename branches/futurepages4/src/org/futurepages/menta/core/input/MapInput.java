package org.futurepages.menta.core.input;

import org.futurepages.menta.exceptions.InputException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * A simple Input that can be used for testing.
 *
 * @author Sergio Oliveira
 */
public class MapInput extends AbstractInput {

    private Map<String, String> headers = null;
    private Map<String, Object> values;
    private Map<String, String> properties = null;
    private Locale loc = Locale.ENGLISH;

    public MapInput() {
        this.values = new HashMap<String, Object>();
    }

    public MapInput(Map<String, Object> values) {
        this.values = values;
    }
    
    public void setLocale(Locale loc) {
    	this.loc = loc;
    }
    
    public void setHeader(String name, String value) {
        if (headers == null) headers = new HashMap<String, String>();
        headers.put(name, value);
    }

	public String getHeader(String name) {
        if (headers != null) {
            return headers.get(name);
        }
        return null;
    }

	public Iterator<String> getHeaderKeys() {

		if (headers == null) headers = new HashMap<String, String>();

		return headers.keySet().iterator();

	}

	public void setProperty(String name, String value) {
		if (properties == null) properties = new HashMap<String, String>();
		properties.put(name, value);
	}

	public String getProperty(String name) {
		if (properties != null) {
			return properties.get(name);
		}

		return null;
	}
	
	public boolean hasValue(String name) {
		
		return values.containsKey(name);
	}
	
	public String getStringValue(String name) {
        Object value = values.get(name);
        if (value != null) return value.toString();
        return null;
    }

	public Iterator<String> keys() {
		return values.keySet().iterator();
	}

	public void removeValue(String name) {
		values.remove(name);
	}

   	public String[] getStringValues(String name) {
		Object obj = values.get(name);
		if (obj == null) return null;
		if (obj instanceof String[]) {
			return (String[]) obj;
		} else if (obj instanceof String) {
			String[] s = new String[1];
			s[0] = (String) obj;
			return s;
		}
		throw new InputException("Error trying to get a String []: " + name);
	}

	public void setValue(String name, Object value) {
		values.put(name, value);
	}

	public Object getValue(String name) {
		return values.get(name);
	}

	protected Locale getLocale() {
		return loc;
	}
 
}
