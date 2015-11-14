package org.futurepages.menta.core.tags;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.futurepages.menta.annotations.SuperTag;
import org.futurepages.menta.annotations.TagAttribute;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.core.tags.cerne.Context;

/**
 * @author Sergio Oliveira
 */
@SuperTag
public abstract class ConditionalTag extends BodyTagSupport {

	@TagAttribute
	protected boolean negate = false;

	@TagAttribute
	protected boolean print = false;

	@TagAttribute (rtexprvalue = false)
	protected boolean context = false;
	
	protected ServletContext application = null;
	protected HttpSession session = null;
	protected HttpServletRequest req = null;
	protected HttpServletResponse res = null;
	protected Action action = null;
	protected Locale loc = null;
	private boolean condition;

	protected void init() {
		this.application = pageContext.getServletContext();
		this.session = pageContext.getSession();
		this.req = (HttpServletRequest) pageContext.getRequest();
		this.res = (HttpServletResponse) pageContext.getResponse();
		this.action = (Action) req.getAttribute(Forward.ACTION_REQUEST);
		this.loc = LocaleManager.getLocale(req);
	}

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	public void setContext(boolean context) {
		this.context = context;
	}

	public boolean isCondition() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public boolean isContext() {
		return context;
	}

	public abstract boolean testCondition() throws JspException;

	@Override
	public int doStartTag() throws JspException {
		init();
		condition = (!negate) ? testCondition() : !testCondition();
		if (!context) {
			if (!print) {
				if (condition) {
					return EVAL_BODY_BUFFERED;
				}
			}
		} else {
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}


	@Override
	public int doAfterBody() throws JspException {
		if (getBodyContent() != null) {
			try {
				getBodyContent()
				.writeOut(getBodyContent().getEnclosingWriter());
			} catch (IOException ex) {
				throw new JspException(ex);
			} finally {
				getBodyContent().clearBody();
			}
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		if (print) {
			try {
				pageContext.getOut().print(String.valueOf(condition));
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return EVAL_PAGE;
	}

	protected Object findValue(String expression) throws JspException {
		javax.servlet.jsp.tagext.Tag parent = findAncestorWithClass(this,
				Context.class);
		return PrintTag.getValue(parent, expression, pageContext, true);
	}
}