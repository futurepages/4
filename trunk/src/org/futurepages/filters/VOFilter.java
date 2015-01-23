package org.futurepages.filters;

import org.futurepages.core.action.Action;
import org.futurepages.core.filter.Filter;
import org.futurepages.exceptions.FilterException;
import org.futurepages.core.control.InvocationChain;
import org.futurepages.util.InjectionUtils;

/**
 * @author Sergio Oliveira
 */
public class VOFilter implements Filter {
	
	public static char PREFIX_SEPARATOR = '.';
    
    private final Class targetClass;
    private String objectKey = null;
    private String prefix = null;
    private boolean tryField = true;
    private boolean convert = true;
    private boolean convertNullToFalse = true;

	private String[] excludedPaths;
    
    public VOFilter(Class klass) {
        
        this.targetClass = klass;
        
    }
    
     public VOFilter(Class klass, boolean tryField) {
         
        this(klass);
        
        this.tryField = tryField;
    }
    
    
    public VOFilter(Class klass, String key) {
        
        this(klass);
        
        this.objectKey = key;
    }
    
    public VOFilter(String key, Class klass) {
    	this(klass, key);
    }

    public VOFilter(String key, Class klass,String... excludedPaths) {
    	this(klass, key);
	    this.excludedPaths = excludedPaths;

    }
    
    public VOFilter(Class klass, String key, boolean tryField) {
        
        this(klass, key);
        
        this.tryField = tryField;
    }

    public VOFilter(Class klass, String key, boolean tryField, String prefix) {

        this(klass, key, tryField);
        
        this.prefix = prefix;

    }
    
    public VOFilter(String key, Class klass, String prefix) {
    	
    	this(klass, key, true, prefix);
    	
    }
    
    public VOFilter(Class klass, String key, boolean tryField, boolean convert, String prefix) {
        
        this(klass, key, tryField, prefix);
        
        this.convert = convert;
        
        
    }   
    
    public void setConvertNullToFalse(boolean convertNullToFalse) {
    	
    	this.convertNullToFalse = convertNullToFalse;
    }
    
	@Override
    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("VOFilter: Class=").append(targetClass.getName()).append(" Key=").append(objectKey != null ? objectKey : targetClass.getName());
        return sb.toString();
    }
    
    private Object getTarget() throws FilterException {
        
        try {
            return targetClass.newInstance();
        } catch(Exception e) {
            throw new FilterException(e);
        }        
    }
    
	@Override
    public String filter(InvocationChain chain) throws Exception {

    	Action action = chain.getAction();

    	Object target = getTarget();
		InjectionUtils.getObject(target, action.getInput(), action.getLocale(), tryField, prefix, convert, convertNullToFalse, excludedPaths);

    	setTarget(action, target);
    	
    	return chain.invoke();
    }
    
    
    private void setTarget(Action action, Object target) {
        
        if (objectKey != null) {
            
            action.getInput().setValue(objectKey, target);
            
        } else {
            
            action.getInput().setValue(targetClass.getName(), target);
        }
    }
    
	@Override
    public void destroy() { }
}
        