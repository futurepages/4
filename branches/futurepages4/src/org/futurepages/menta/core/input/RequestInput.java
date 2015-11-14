package org.futurepages.menta.core.input;

import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.exceptions.InputException;
import org.futurepages.util.EnumerationUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Encapsulates a HttpServletRequest as an Input for Mentawai actions. This
 * class also implements the java.util.Map interface so it can be used with JSP
 * Expression Language. Most of map operations are not supported.
 *
 * @author Sergio Oliveira
 * @version $Id: InputRequest.java,v 1.1 2006/03/02 17:11:36 soliveira Exp $
 */
public class RequestInput extends AbstractInput implements Map<String, Object> {

	protected Map<String, Object> map;

	protected HttpServletRequest req;

	protected static Map<String, Method> cache = null;
	
	protected Set<String> keys = new TreeSet<String>();

	/**
	 * Creates an Input for the given HttpServletRequest.
	 *
	 * @param req The request
	 */
	public RequestInput(HttpServletRequest req) {
		Map<String, ? extends Object> mapReq = req.getParameterMap();

		// map is ready-only !!!
		// that's why the clone...
		this.map = clone(mapReq);

		// Path-info is not supported by *.fpg web.xml mapping!
		/*
		 * String pathInfo = req.getPathInfo(); if (pathInfo != null &&
		 * pathInfo.equals("")) { map.put("pathInfo", pathInfo); }
		 */
		this.req = req;
	}

	/*
	 * This is necessary because the map returned by the request is read-only.
	 * Input parameters are not read-only and can be modified with the setValue
	 * method.
	 */
	private Map<String, Object> clone(Map<String, ? extends Object> map) {
		Map<String, Object> clone = new HashMap<String, Object>();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Object value = map.get(key);
			if (value instanceof String[]) {
				String[] array = (String[]) value;
				if (array.length == 1) {
					value = array[0];
				}
			}
			clone.put(key, value);
			keys.add(key);
		}
		return clone;
	}

	@Override
	public String toString() {
		Iterator<String> iter = keys();
		StringBuffer sb = new StringBuffer(1024);
		while (iter.hasNext()) {
			String name = (String) iter.next();
			String value = getStringValue(name);
			value = value.replace('\n', ' ');
			sb.append(name).append(" = ").append(value).append("\n");
		}
		return sb.toString();
	}

	/**
	 * Gets the HttpServletRequest this input encapsulates.
	 *
	 * @return The request this input encapsulates
	 */
	public HttpServletRequest getRequest() {
		return req;
	}

	@Override
	public String getProperty(String name) {

		StringBuffer sb = new StringBuffer(64);

		sb.append("get");
		sb.append(name.substring(0, 1).toUpperCase());
		if (name.length() > 1)
			sb.append(name.substring(1));

		String methodName = sb.toString();

		try {
			Method m = null;
			
			if (cache == null)
				cache = new Hashtable<String, Method>();
			else {
				m = cache.get(methodName);
			}
			if (m == null) {
				m = req.getClass().getMethod(methodName, new Class[0]);
				cache.put(methodName, m);
			}
			Object value = m.invoke(req, new Object[0]);

			if (value != null)
				return value.toString();

		} catch (Exception e) {
			// nothing here on purpose...
		}
		return null;
	}

	@Override
	public String getHeader(String name) {
		return req.getHeader(name);
	}

	@Override
	public Iterator<String> getHeaderKeys() {

		return EnumerationUtil.toIterator(req.getHeaderNames());

	}

	@Override
	public Iterator<String> keys() {
		return keys.iterator();
	}

	@Override
	public void removeValue(String name) {
		map.remove(name);
		keys.remove(name);
	}

	/**
	 * If the parameter is not a String, its toString() method is called.
	 */
	@Override
	public String getStringValue(String name) {
		Object o = map.get(name);
		return (o != null ? o.toString() : null);
	}

	/**
	 * If the parameter is not a String or a String array, a InputException will
	 * be thrown. If the parameter is a String, a String array of size one with
	 * the string is returned.
	 */
	@Override
	public String[] getStringValues(String name) {
		Object obj = map.get(name);
		if (obj == null)
			return null;
		if (obj instanceof String[]) {
			return (String[]) obj;
		} else if (obj instanceof String) {
			String[] s = new String[1];
			s[0] = (String) obj;
			return s;
		}
		throw new InputException("Error trying to get a String []: " + name);
	}

	@Override
	public void setValue(String name, Object value) {
		map.put(name, value);
		keys.add(name);
	}

	@Override
	public boolean hasValue(String name) {
		
		return map.containsKey(name);
	}

	@Override
	public Object getValue(String name) {
		return map.get(name);
	}

	@Override
	public void clear() {
		map.clear();
		keys.clear();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		if (key instanceof String) {
			return map.containsKey((String) key);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		if (key instanceof String) {
			return getValue((String) key);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Set<String> keySet() {
		if (size() == 0) return new HashSet<String>(0);
		Set<String> set = new HashSet<String>(size());
		Iterator<String> iter = keys();
		while(iter.hasNext()) {	
			set.add(iter.next());
		}
		return set;
	}

	@Override
	public Object put(String key, Object value) {
		if (key instanceof String) {
			setValue((String) key, value);
			return value;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public void putAll(Map<? extends String,? extends Object> t) {
		map.putAll(t);
		
		Iterator<? extends String> iter = t.keySet().iterator();
		
		while(iter.hasNext()) {
			
			keys.add(iter.next());
		}
	}

	@Override
	public Object remove(Object key) {
		if (key instanceof String) {
			String s = (String) key;
			Object obj = getValue(s);
			if (obj != null) {
				removeValue(s);
				return obj;
			}
			return null;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Entry<String,Object>> entrySet() {
		return map.entrySet();
	}
	
	@Override
	protected Locale getLocale() {

		return LocaleManager.getLocale(req);
	}
}