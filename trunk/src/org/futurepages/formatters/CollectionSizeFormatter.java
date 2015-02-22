package org.futurepages.formatters;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.futurepages.core.formatter.AbstractFormatter;

/**
 * Exibe a quantidade de elementos da coleção.
 * .
 */
public class CollectionSizeFormatter extends AbstractFormatter {
    
    public String format(Object value, Locale loc) {
		if(value instanceof Object[]){
			return String.valueOf(((Object[])value).length);
		}else if(value instanceof Map){
			return String.valueOf(((Map)value).size());
	    }
        return String.valueOf(((Collection) value).size());
    }
}