package org.futurepages.util.templatizer.expressions.tree;

import org.futurepages.util.templatizer.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public interface Exp {

	public Object eval(ContextTemplateTag context);

	public void toString(StringBuilder sb);
}
