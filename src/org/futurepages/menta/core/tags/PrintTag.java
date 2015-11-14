package org.futurepages.menta.core.tags;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.menta.annotations.SuperTag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.core.tags.cerne.Context;

/**
 * A very easy abstract class to create custom tags that print something to the screen.
 *
 * @author Sergio Oliveira
 */
@SuperTag
public abstract class PrintTag extends BodyTagSupport {
    
	protected ServletContext application = null;
    protected HttpSession session = null;
    protected HttpServletRequest req = null;
    protected HttpServletResponse res = null;
	protected Action action = null;
	protected Locale loc = null;
    
	@TagAttribute(name  = "max")
    protected int maxToPrint = -1;
    
    public void setMax(int maxToPrint) {
        this.maxToPrint = maxToPrint;
    }
    
    /**
     * Override this method to return what you want to print in the screen.
     * 
     * @return The string to print.
     */
    public abstract String getStringToPrint() throws JspException;
    
	public static Boolean getBooleanValue(Object bean, String value) {
		try {
			StringBuffer sb = new StringBuffer(50);
			sb.append("is");
			sb.append(value.substring(0, 1).toUpperCase());
			if (value.length() > 1) sb.append(value.substring(1));
			Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
			if (m != null) {
                m.setAccessible(true);
				Object obj = m.invoke(bean, new Object[0]);
                if (obj instanceof Boolean) {
                    return (Boolean) obj;
                }
			}
		}
		catch (Exception e) {
			//DefaultExceptionLogger.getInstance().execute(e);
		}
        
		try {
			StringBuffer sb = new StringBuffer(50);
			sb.append("has");
			sb.append(value.substring(0, 1).toUpperCase());
			if (value.length() > 1) sb.append(value.substring(1));
			Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
			if (m != null) {
				Object obj = m.invoke(bean, new Object[0]);
                if (obj instanceof Boolean) {
                    return (Boolean) obj;
                }                
			}
		}
		catch (Exception e) {
			//DefaultExceptionLogger.getInstance().execute(e);
		}        
        
		return null;
	}    
    
    /**
     * Finds a value through reflection.
     *
     * @param bean The object in where to look for the value.
     * @param name The name of the attribute.
     * @param tryBoolean Should I try isXXX and hasXXX ?
     * @return The value found by reflection or null.
     * @since 1.1.1
     */
	public static Object getValue(Object bean, String name, boolean tryBoolean) {
		try {
			StringBuffer sb = new StringBuffer(50);
			sb.append("get");
			sb.append(name.substring(0, 1).toUpperCase());
			if (name.length() > 1) sb.append(name.substring(1));
			Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
			if (m != null) {
                m.setAccessible(true);
				return m.invoke(bean, new Object[0]);
			}
		}
		catch (Exception e) {
			//DefaultExceptionLogger.getInstance().execute(e);
		}
        if (tryBoolean) {
		    return getBooleanValue(bean, name);
        }
        return null;
	}    
    
    /**
     * Finds a value for the corresponding expression.
     * This is useful to look for expressions like user.name.firstName.
     * It works pretty much like a JSP Expression Language.
     *
     * @param expression The expression to look for.
     * @param pageContext The pageContext of the tag.
     * @param tryBoolean Should I try isXXX and has XXX ?
     * @return The value corresponding to the expression.
     * @since 1.1.1
     */
    public static Object getValue(String expression, PageContext pageContext, boolean tryBoolean) {
        StringTokenizer st = new StringTokenizer(expression, ".");
        if (st.countTokens() == 1) {
            //return findValue(st.nextToken(), pageContext);
            return pageContext.findAttribute(st.nextToken());
        } else if (st.countTokens() > 1) {
            String first = st.nextToken();
            //Object value = findValue(first, pageContext);
            Object value = pageContext.findAttribute(first);
            if (value == null) return null;
            while(st.hasMoreTokens()) {
                String next = st.nextToken();
                if (value instanceof Map) {
                    Map map = (Map) value;
                    value = map.get(next);
                } else {
                    value = getValue(value, next, tryBoolean);
                }
                if (value == null) return null;
            }
            return value;
        } else {
        	return null; // expression was blank ???
        }
    }
    
    /**
     * Finds a value for the corresponding expression.
     * This is useful to look for expressions like user.name.firstName.
     * It works pretty much like a JSP Expression Language, but it searches a java Object.
     *
     * @param expression The expression to look for.
     * @param bean The bean where to search.
     * @param tryBoolean Should I try isXXX and has XXX ?
     * @return The value corresponding to the expression.
     * @since 1.3
     */
    public static Object getValue(String expression, Object bean, boolean tryBoolean) {
        StringTokenizer st = new StringTokenizer(expression, ".");
        if (st.countTokens() == 1) {
            //return findValue(st.nextToken(), pageContext);
            return getValue(bean, st.nextToken(), tryBoolean);
        } else {
            String first = st.nextToken();
            //Object value = findValue(first, pageContext);
            Object value = getValue(bean, first, false);
            if (value == null) return null;
            while(st.hasMoreTokens()) {
                String next = st.nextToken();
                if (value instanceof Map) {
                    Map map = (Map) value;
                    value = map.get(next);
                } else {
                    value = getValue(value, next, (tryBoolean && !st.hasMoreTokens()));
                }
                if (value == null) return null;
            }
            return value;
        }
    }        
    
    @Override
    public int doStartTag() throws JspException {
    	this.application = pageContext.getServletContext();
        this.session = pageContext.getSession();
        this.req = (HttpServletRequest) pageContext.getRequest();
        this.res = (HttpServletResponse) pageContext.getResponse();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.getLocale(req);
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        String s = getStringToPrint();
        if (s != null) {
            
            if (maxToPrint > 0 && s.length() > maxToPrint) {
                
                s = s.substring(0, maxToPrint) + "...";
                
            }
            
            try {
                pageContext.getOut().print(s);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }        
        return EVAL_PAGE;
    }

    protected Object findValue(String expression) throws JspException{
        javax.servlet.jsp.tagext.Tag parent = findAncestorWithClass(this, Context.class);
        return getValue(parent, expression, pageContext, true);
    }

    public static Object getValue(Tag context, String value, PageContext pageContext, boolean tryBoolean) throws JspException {

        if (context != null) {

            Context ctx = (Context) context;

            Object obj = ctx.getObject();

            if (obj == null) {

                if (value == null) {

                    throw new JspException("Out tag cannot find value: context is null!");

                } else {

                    Object object = PrintTag.getValue(value, pageContext, tryBoolean);

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

                    object = PrintTag.getValue(value, obj, tryBoolean);

                    if (object != null) {
                        // for bean...
                        return object;
                    } else {
                        // try output...
                        object = PrintTag.getValue(value, pageContext, tryBoolean);
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
                Object obj = PrintTag.getValue(value, pageContext, tryBoolean);
                if (obj == null) return null;
                return obj;
            }
        }
    }
}