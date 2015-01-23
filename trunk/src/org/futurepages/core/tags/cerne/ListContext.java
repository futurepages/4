package org.futurepages.core.tags.cerne;

import java.util.List;

import javax.servlet.jsp.JspException;

/**
 * @author Sergio Oliveira
 */
public interface ListContext<T extends Object> {
    
    public List<T> getList() throws JspException;

	public String getVar();
}
