package org.futurepages.tags.core;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.Context;
import org.futurepages.core.tags.cerne.ListContext;
import org.futurepages.core.tags.cerne.LoopTag;

/**
 * @author Sergio Oliveira
 */
@org.futurepages.annotations.Tag(bodyContent = ContentTypeEnum.JSP)
public class Loop extends LoopTag implements Context {

    private static final String COUNTER_VAR = "counter";

    private List<Object> list = null;

    private int currIndex = -1;

    @TagAttribute(name = "var")
    private String varname = null;

    @TagAttribute
    private String counter = COUNTER_VAR;

    public void setVar(String var) {
        this.varname = var;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    private void setNext() {
        currIndex++;
        if (varname != null)
            pageContext.setAttribute(varname, list.get(currIndex),
                    PageContext.PAGE_SCOPE);
        if (counter != null)
            pageContext.setAttribute(counter, new Integer(currIndex),
                    PageContext.PAGE_SCOPE);
    }

    private boolean hasMore() {
        if (list == null)
            return false;
        return (currIndex >= -1 && currIndex < list.size() - 1);
    }

    public Object getObject() {
        return list.get(currIndex);
    }

    public void init() throws JspException {
        // Pega uma lista de algum lugar para loopar...
        Tag parent = findAncestorWithClass(this, ListContext.class);
        if (parent != null) {
            ListContext ctx = (ListContext) parent;
            this.list = (List) pageContext.getAttribute(ctx.getVar(), PageContext.PAGE_SCOPE);
//			this.list = ctx.getList(); //antigamente era assim, mas desta forma, o getList() estava sempre sendo chamado duas vezes.
        } else {
            throw new JspException("Loop not enclosed by a ListContext !!!");
        }
    }

	@Override
    public boolean loopCondition() {
        if (hasMore()) {
            setNext();
            return true;
        }
        return false;
    }

	@Override
    public void afterEachLoop() throws JspException {

        // no need to do anything...

    }

    /*
     * public int doEndTag() throws JspException { list = null; currIndex = -1;
     * pageContext.removeAttribute(varname); varname = VARNAME; return
     * super.doEndTag(); }
     */

	@Override
    public int doEndTag() throws JspException {
        if (varname != null)
            pageContext.removeAttribute(varname, PageContext.PAGE_SCOPE);
        if (counter != null)
            pageContext.removeAttribute(counter, PageContext.PAGE_SCOPE);
        currIndex = -1;
        return EVAL_PAGE;
    }
}
