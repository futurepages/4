package org.futurepages.tags;

import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.action.Action;
import org.futurepages.core.tags.PrintTag;
import org.futurepages.core.input.Input;
import org.futurepages.core.output.Output;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.core.formatter.FormatterManager;
import org.futurepages.core.i18n.LocaleManager;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.Context;

/**
 * @author Sergio Oliveira
 * @deprecated utilizar EL e a tag {@link ValueFormatter} para uso com formatadores e max
 */
@Deprecated // utilizar EL 
@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class Out extends PrintTag {

	@TagAttribute
	private String value = null;

	@TagAttribute
    private String formatter = null;
	
	@TagAttribute
    private String onBlank = null;
	
	@TagAttribute
    private String onFalse = null;
	
	@TagAttribute
    private String onTrue = null;
	
	@TagAttribute
    private boolean includeTime = false;
    
	private boolean onlyTime = false;
    
    public void setOnlyTime(boolean onlyTime) {
    	this.onlyTime = onlyTime;
    }
    
    public void setIncludeTime(boolean includeTime) {
    	
    	this.includeTime = includeTime;
    }

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setOnFalse(String onFalse) {
		
		this.onFalse = onFalse;
	}
	
	public void setOnTrue(String onTrue) {
		
		this.onTrue = onTrue;
	}
	
	public void setOnBlank(String onBlank) {
		this.onBlank = onBlank;
	}
    
    public void setFormatter(String formatter) {
    	this.formatter = formatter;
    }

    /*
	private static Object findValue(String value, PageContext pageContext) {
		if (action == null) return null;
		Output output = action.getOutput();
        Object obj = output.getValue(value);
        if (obj == null) {
            org.mentawai.core.Context session = action.getSession();
            obj = session.getAttribute(value);
            if (obj == null) {
                org.mentawai.core.Context application = action.getApplication();
                obj = application.getAttribute(value);
            }
        }
        return obj;
	}
    */
    
    public static Object getValue(Tag context, String value, PageContext pageContext, boolean tryBoolean) throws JspException {
    	
        if (context != null) {
        	
            Context ctx = (Context) context;
            
            Object obj = ctx.getObject();
            
            if (obj == null) {
            	
                if (value == null) {
                	
                    throw new JspException("Out tag cannot find value: context is null!");
                	
                } else {
                	
                    Object object = getValue(value, pageContext, tryBoolean);
                    
                    if (object == null) {
                    	return null; //byLeandro (achei melhor retornar null - antes era a exception a seguir:
                        //throw new JspException("Out tag cannot find value: " + value);
                    	
                    } else {
                    	
                        return object;
                    }
                }
                
            } else {
                if (value == null) {
                    // for loop...
                    return obj;
                } else {
                    Object object = null;
                    
                    /*
                    if (tryBoolean) {
                        object = getBooleanValue(obj, value);
                        if (object == null) {
                            object = getValue(obj, value, false);
                        }
                    } else {
                        object = getValue(obj, value, false);
                    }
                    */
                    
                    object = getValue(value, obj, tryBoolean);
                    
                    if (object != null) {
                        // for bean...
                        return object;
                    } else {
                        // try output...
                        object = getValue(value, pageContext, tryBoolean);
                        if (object == null) {
                            // not found in bean...
                            return null;
                        } else {
                            // for output...
                            return object;
                        }
                    }
                }
            }
        } else {
            if (value == null) {
                throw new JspException("Not inclosed by a context tag!");
            } else {
                //if (action == null) throw new JspException("No action in request for tag: " + value);
                Object obj = getValue(value, pageContext, tryBoolean);
                if (obj == null) return null;
                return obj;            
            }
        }   
    }
       
    private String checkOnBlank(String value) {
    	
    	if ((value == null || value.equals("")) && onBlank != null) {
    		
    		return onBlank;
    	}
    	
    	return value;
    }
    
    private Object findValue(Action action, String value) {
//Antes:
//     if (action == null) return null;
//Depois {
       //@byLeandro
       if (action == null){
                return req.getParameter(value);
       }
       //end_@byLeandro
//}

		Output output = action.getOutput();
		Input input = action.getInput();
		
		Object obj = output.getValue(value);
		
		if (obj == null) {
			
			obj = input.getValue(value);

		}
		
		return obj;
    }

	@Override
	public String getStringToPrint() throws JspException {
		
		Tag parent = findAncestorWithClass(this, Context.class);
		
		Object obj = null;
		
		try {
		
			obj = getValue(parent, value, pageContext, true);
			
		} catch(JspException e) {
			
			// anything is better than exception... try output and input before throwing exception...
			
	        if (action == null) throw e;
	        
	        obj = findValue(action, value);
	        
			if (obj == null) throw e;
		}
        
        if (obj == null) {
        	
        	obj = findValue(action, value);
        	
        	if (obj == null) {
        	
	        	if (onBlank == null) {
	        		
	    			return "";
	        		
	        	} else {
	        		
	        		return onBlank;
	        	}
        	
        	}
        }
        
        // formatter logic here:
        
        if (formatter != null) {
            
            Formatter f = FormatterManager.getFormatter(formatter);
            
            if (f == null) throw new JspException("Cannot find formatter: " + formatter);
            
            return checkOnBlank(f.format(obj, loc));
        	
        } else if (obj instanceof java.util.Date) {
        	
        	// if formatter was not defined but we have a default data formatter, then use it...
        	
        	Formatter f;
        	
        	StringBuilder sb = new StringBuilder(32);
        	
        	if (!onlyTime) {
        	
	        	if (( f = FormatterManager.getFixedDateFormatter()) != null) {
	        		
	        		//return checkOnBlank(f.format(obj, loc));
	        		
	        		sb.append(f.format(obj, loc));
	        		
	        	}
	        	
	        	if (sb.length() == 0) {
	        	
		        	SimpleDateFormat format = LocaleManager.getSimpleDateFormat(loc);
		        	
		        	if (format != null) {
		        		
		        		synchronized(format) {
		        		
		        			//return checkOnBlank(format.format(obj));
		        			
		        			sb.append(format.format(obj));
		        			
		        		}
		        	}
	        	
	        	}
        	}
        	
        	if (!includeTime && !onlyTime) {
        		
        		return checkOnBlank(sb.toString());
        		
        	} else {
        		
        		boolean found = false;
        		
	        	if (( f = FormatterManager.getFixedTimeFormatter()) != null) {
	        		
	        		//return checkOnBlank(f.format(obj, loc));
	        		
	        		found = true;
	        		
	        		if (!onlyTime) sb.append(' ');
	        		
	        		sb.append(f.format(obj, loc));
	        		
	        	}
	        	
	        	if (!found) {
	        	
		        	SimpleDateFormat format = LocaleManager.getSimpleTimeFormat(loc);
		        	
		        	if (format != null) {
		        		
		        		synchronized(format) {
		        		
		        			//return checkOnBlank(format.format(obj));
		        			
		        			if (!onlyTime) sb.append(' ');
		        			
		        			sb.append(format.format(obj));
		        			
		        		}
		        	}
	        	}
	        	
	        	return checkOnBlank(sb.toString());
        	}
        	
        	
        } else if (obj instanceof Boolean) {
        	
        	boolean b = ((Boolean) obj).booleanValue();
        	
        	if (b && onTrue != null) return onTrue;
        	
        	if (!b && onFalse != null) return onFalse;
        }
        
        return checkOnBlank(obj.toString());
	}
}