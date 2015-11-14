package org.futurepages.menta.core.tags.cerne;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.menta.annotations.SuperTag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.input.Input;
import org.futurepages.menta.core.output.Output;
import org.futurepages.menta.core.tags.PrintTag;

/**
 * @author Sergio Oliveira
 */
@SuperTag
public abstract class HTMLTag extends PrintTag {
    
    private static final String SEPARATOR = "#";
    
    @TagAttribute
    protected String extra;
    
    @TagAttribute
    private String separator = SEPARATOR;
    
    public void setExtra(String extra) {
        
        this.extra = extra;
        
    }
    
    public void setSeparator(String separator) {
        
        this.separator = separator;
        
    }

    protected String getExtraAttributes() {
        
        if (extra == null) return "";
        
        StringBuffer sb = new StringBuffer(512);
        
        String[] s = extra.split("\\" + separator);
        
        for(int i=0;i<s.length;i++) {
            
            String[] ss = s[i].split("=");
            
            if (ss.length != 2) continue;
            
            sb.append(" ").append(ss[0].trim()).append("=\"").append(ss[1].trim()).append('"');
            
        }
        
        return sb.toString();
    }
    
    protected Object findObject(String name) {
    	
    	return findObject(name, false);
    	
    }
    
    protected Object findObject(String name, boolean tryBoolean) {
        
        Object value = null;
        
        Tag parent = findAncestorWithClass(this, Context.class);
        
        if (parent != null) {
        
	        try {
	            
	            value = PrintTag.getValue(parent, name, pageContext, tryBoolean);
	            
	            if (value != null && !value.equals("")) return value;
	            
	        } catch(JspException e) {	            
	        }
        
        }
        
        if (action == null) return null;
        
		Output output = action.getOutput();
		Input input = action.getInput();
		
		value = output.getValue(name);
		if (value == null) {
			value = input.getValue(name);
		}
        return value;
    }    
    
    protected String findValue(String name) {
        Object value = findObject(name);
        if (value == null) return null;
        return value.toString();
    }
    
    protected boolean contains(int [] values, int x) {
        if (values == null) return false;
		for(int i=0;i<values.length;i++) {
			if (values[i] == x) return true;
		}
        return false;
    }
    
    protected boolean contains(String [] values, String x) {
        if (values == null) return false;
		for(int i=0;i<values.length;i++) {
			if (values[i] != null && values[i].equals(x)) return true;
		}
        return false;
    }	
    
	protected String[] findValues(String name) {
        Object value = findObject(name);
        
		if (value == null) return null;
		
		if (value instanceof int[]) {
			
			int[] x = (int[]) value;
			
			String[] s = new String[x.length];
			
			for(int i=0;i<x.length;i++) {
				
				s[i] = String.valueOf(x[i]);
			}
			
			return s;
		}
		
		if (value instanceof String[]) {
			
			return (String[]) value;
		}
		
		if (value instanceof Integer) {
			String[] x = new String[1];
			x[0] = String.valueOf(((Integer) value).intValue());
			return x;
		}
		
		if (value instanceof String) {
			String[] x = new String[1];
			x[0] = (String) value;
			return x;
		}
      
      if (value instanceof Collection) {
         
         Collection c = (Collection) value;
         
         String[] x = new String[c.size()];
         
         int index = 0;
         
         Iterator iter = c.iterator();
         
         while(iter.hasNext()) {
            
            x[index++] = iter.next().toString();
            
         }
         
         return x;
      }
		
		return null;
	}
    
}

