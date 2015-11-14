package org.futurepages.menta.core.tags.cerne;

import javax.servlet.jsp.JspException;

/**
 * @author Sergio Oliveira
 */
public interface Context {
    
    public Object getObject() throws JspException;
    
}
