package org.futurepages.core.formatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergio Oliveira
 */
public class FormatterManager {
    
    private static Map<String, Formatter> formatters = new HashMap<String, Formatter>();
    
    private static Formatter fixedDateFormatter = null;
    private static Formatter fixedTimeFormatter = null;
    
    public static void addFormatter(String name, Formatter f) {
        
        formatters.put(name, f);
    }
    
    public static Formatter getFormatter(String name) {
        
        return formatters.get(name);
        
    }
    
    public static void setFixedDateFormatter(Formatter f) {
    	
    	fixedDateFormatter = f;
    }
    
    public static Formatter getFixedDateFormatter() {
    	
    	return fixedDateFormatter;
    }
    
    public static void setFixedTimeFormatter(Formatter f) {
    	
    	fixedTimeFormatter = f;
    }
    
    public static Formatter getFixedTimeFormatter() {
    	
    	return fixedTimeFormatter;
    }
    
    
    public static void init() {
    }
}
