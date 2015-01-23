package org.futurepages.core.tags.cerne;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.futurepages.annotations.SuperTag;
import org.futurepages.annotations.TagAttribute;

import org.futurepages.core.action.Action;
import org.futurepages.consequences.Forward;
import org.futurepages.core.i18n.LocaleManager;

/**
 * @author Sergio Oliveira
 */
@SuperTag
public abstract class AbstractContext extends BodyTagSupport implements Context {
    
	protected ServletContext application = null;
    protected HttpSession session = null;
    protected HttpServletRequest req = null;
    protected HttpServletResponse res = null;
	protected Locale loc = null;
	protected Action action = null;

	@TagAttribute
    protected String var = null;

    public void setVar(String var) {
        this.var = var;
    }
    
    private String getVar() {
        if (var == null) return getName();
        return var;
    }
    
    protected abstract String getName();
    
	@Override
    public int doStartTag() throws JspException {
    	this.application = pageContext.getServletContext();
        this.session = pageContext.getSession();
        this.req = (HttpServletRequest) pageContext.getRequest();
        this.res = (HttpServletResponse) pageContext.getResponse();
		this.loc = LocaleManager.getLocale(req);
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
        
        Object object = getObject();
        if (object != null) {
            pageContext.setAttribute(getVar(), object, PageContext.PAGE_SCOPE);
        }
        
        return super.doStartTag();
    }
	
	@Override
    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        try {
             if (bc != null) bc.writeOut(bc.getEnclosingWriter());
        } catch(IOException e) {
            throw new JspException(e);
        } finally {
            if (bc != null) bc.clearBody();
        }
        return SKIP_BODY;
    }	
    
	@Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(getVar(), PageContext.PAGE_SCOPE);
        return super.doEndTag();
    }
	
}



    
