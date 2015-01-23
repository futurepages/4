package org.futurepages.core.i18n;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author Sergio Oliveira
 */
public class I18N {

    private Properties props = null;

    public I18N(Properties props) {
        this.props = props;
    }
	
	public String get(int key) {
		return get(String.valueOf(key));
	}
    
    public boolean hasKey(String key) {
        String text = props.getProperty(key);
        return text != null;
    }

    public String get(String key) {
        String text = props.getProperty(key);
        if (text == null) return key;
        return text;
    }
	
	public Iterator<String> keys() {
		Enumeration en = props.propertyNames();
		HashSet<String> set = new HashSet<String>();
		while(en.hasMoreElements()) {
			String key = (String) en.nextElement();
			set.add(key);
		}
		return set.iterator();
	}
}