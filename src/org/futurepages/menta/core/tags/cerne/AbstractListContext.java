package org.futurepages.menta.core.tags.cerne;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.futurepages.menta.actions.NullAction;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.i18n.LocaleManager;

/**
 * @author Sergio Oliveira
 */
public abstract class AbstractListContext<T extends Object> extends BodyTagSupport implements ListContext<T> {

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

    public String getVar() {
        if (var == null)
            return getName();
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
		if(this.action==null){
			this.action = new NullAction(req,res);
		}

        List<T> list = getList();
        if (list != null) {
            pageContext.setAttribute(getVar(), list, PageContext.PAGE_SCOPE);
        }

        return super.doStartTag();
    }

	@Override
    public int doAfterBody() throws JspException {
        BodyContent bc = getBodyContent();
        try {
            if (bc != null)
                bc.writeOut(bc.getEnclosingWriter());
        } catch (IOException e) {
            throw new JspException(e);
        } finally {
            if (bc != null)
                bc.clearBody();
        }
        return SKIP_BODY;
    }

	@Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(getVar(), PageContext.PAGE_SCOPE);
        return super.doEndTag();
    }
}
