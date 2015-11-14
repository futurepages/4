package org.futurepages.menta.core.formatter;

import org.futurepages.core.formatter.AbstractFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergio Oliveira
 */
public class FormatterManager {
    
    private static Map<String, AbstractFormatter> formatters = new HashMap<String, AbstractFormatter>();
    
    private static AbstractFormatter fixedDateFormatter = null;
    private static AbstractFormatter fixedTimeFormatter = null;
    
    public static void addFormatter(String name, AbstractFormatter f) {
        
        formatters.put(name, f);
    }
    
    public static AbstractFormatter getFormatter(String name) {
        
        return formatters.get(name);
        
    }
    
    public static void setFixedDateFormatter(AbstractFormatter f) {
    	
    	fixedDateFormatter = f;
    }
    
    public static AbstractFormatter getFixedDateFormatter() {
    	
    	return fixedDateFormatter;
    }
    
    public static void setFixedTimeFormatter(AbstractFormatter f) {
    	fixedTimeFormatter = f;
    }
    
    public static AbstractFormatter getFixedTimeFormatter() {
    	
    	return fixedTimeFormatter;
    }
    
    
    public static void init() {
    }
}
