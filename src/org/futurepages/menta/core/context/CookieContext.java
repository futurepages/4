package org.futurepages.menta.core.context;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A cookie management abstraction into a Mentawai context.
 * 
 * Note that this class is smart enough to keep track of which cookies were added, which ones were removed
 * and which ones did not suffer any modification and should not be sent again to the browser.
 * 
 * @author Sergio Oliveira
 * @since 1.2
 */
public class CookieContext implements Context, Map<String, Object> {
    
    private final HttpServletRequest req;
    private final HttpServletResponse res;
    
    /**
     * Creates a new CookieContext for this request and response.
     * 
     * @param req The request from where to get the cookies.
     * @param res The response where to put the new cookies.
     */
    public CookieContext(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }
    
    /**
     * Return the cookie value with the given name, if present.
     * 
     * @param name The name of the cookie to return
     * @return The cookie value as a String. 
     */
	@Override
    public Object getAttribute(String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
	        for (Cookie cookie : cookies) {
		        if (cookie.getName().equals(name)) {
			        return cookie.getValue();
		        }
	        }
        }        
        return null;
    }
    
	@Override
    public Iterator<String> keys() {
    	List<String> list = new ArrayList<>();
    	Cookie[] cookies = req.getCookies();
    	if (cookies != null) {
		    for (Cookie cookie : cookies) {
			    list.add(cookie.getName());
		    }
    	}
    	return list.iterator();
    }
    
    /**
     * Sets a cookie to send to the client in the response. 
     * 
     * Note that this method can take a Cookie as the value as well as a String.
     * 
     * If you pass a String, a Cookie object is created with default values for path and max age.
     * 
     * If you pass an Object other than a Cookie or a String, its toString() method its called and
     * taken as the cookie value.
     * 
     * @param name The name of this cookie
     * @param value The cookie object or the value of the cookie as a String
     */
	@Override
    public void setAttribute(String name, Object value) {
        if (value instanceof Cookie) {
            res.addCookie((Cookie) value);
        } else {
            Cookie c = new Cookie(name, value.toString());
            c.setMaxAge(31104000);
            c.setPath("/");
            res.addCookie(c);
        }
    }
    
    /**
     * Tell the browser to remove the given cookie.
     * 
     * @param name The name of the cookie to remove.
     */
	@Override
    public void removeAttribute(String name) {
		Cookie c=null;
		Cookie[] cookies = req.getCookies();
        if (cookies != null) {
	        for (Cookie cookie : cookies) {
		        if (cookie.getName().equals(name)) {
			        c = cookie;
			        break;
		        }
	        }
        }
		if(c!=null){
			c.setMaxAge(0);
			c.setPath("/");
			c.setValue("");
			res.addCookie(c);
		}
   }
    
	@Override
    public void reset() {
        throw new UnsupportedOperationException("reset() is not supported by CookieContext !");
    }
    
	@Override
    public boolean hasAttribute(String name) {
        return getAttribute(name) != null;
        
    }
    
    //// Map methods...
    
	@Override
    public void clear() {
    	
    	throw new UnsupportedOperationException();
    }
    
	@Override
    public boolean containsKey(Object key) {
    	return getAttribute(key.toString()) != null;
    }
    
	@Override
    public boolean containsValue(Object value) {
    	if (!(value instanceof String)) return false;
    	String v = value.toString();
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
	        for (Cookie item : cookies) {
		        String cookie = item.getValue();
		        if (cookie != null && cookie.equals(v)) {
			        return true;
		        }
	        }
        }
        return false;
    }
    
	@Override
    public Set<Entry<String,Object>> entrySet() {
    	throw new UnsupportedOperationException();
    }
    
	@Override
    public Object get(Object key) {
    	if (key == null) return null;
    	Object value = getAttribute(key.toString());
    	if (value == null) return null;
    	return value.toString();
    }
    
	@Override
    public boolean isEmpty() {
    	Cookie[] cookies = req.getCookies();
    	return cookies == null || cookies.length == 0;
    }
    
	@Override
    public Set<String> keySet() {
    	Set<String> keys = new HashSet<>();
    	Iterator<String> iter = keys();
    	while(iter.hasNext()) {
    		keys.add(iter.next());
    	}
    	return keys;
    }
    
	@Override
    public String put(String key, Object value) {
    	setAttribute(key, value);
    	return null;
    }
    
	@Override
	@SuppressWarnings("NullableProblems")
    public void putAll(Map<? extends String,?> t) {
    	throw new UnsupportedOperationException();
    }
    
	@Override
    public String remove(Object key) {
    	removeAttribute(key.toString());
    	return null;
    }
    
	@Override
    public int size() {
    	Cookie[] cookies = req.getCookies();
    	if (cookies == null) return 0;
    	return cookies.length;
    }
    
	@Override
    public Collection<Object> values() {
    	Cookie[] cookies = req.getCookies();
    	if (cookies == null || cookies.length == 0) {
    		return new ArrayList<>(0);
    	}
    	List<Object> list = new ArrayList<>();
		for (Cookie cookie : cookies) {
			list.add(cookie.getValue());
		}
    	return list;
    }
}