package org.futurepages.core.tags.cerne;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @author Sergio Oliveira
 */
public abstract class LoopTag extends BodyTagSupport {
    
	protected ServletContext application = null;
    protected HttpSession session = null;
    protected HttpServletRequest request = null;
    protected HttpServletResponse response = null;
    
    public abstract void init() throws JspException;
    
    public abstract boolean loopCondition() throws JspException;
    
    public abstract void afterEachLoop() throws JspException;

	@Override
    public int doStartTag() throws JspException {
    	this.application = pageContext.getServletContext();
        this.session = pageContext.getSession();
        this.request = (HttpServletRequest) pageContext.getRequest();
        this.response = (HttpServletResponse) pageContext.getResponse();
        
        init();
        
        if (loopCondition()) return EVAL_BODY_BUFFERED;
        return SKIP_BODY;
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
        
        afterEachLoop();
        
        if (loopCondition()) return EVAL_BODY_AGAIN;
        return SKIP_BODY;
    }
}

    
