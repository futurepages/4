package org.futurepages.util.template.simpletemplate.expressions.tree;

import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public interface Exp {

	public Object eval(ContextTemplateTag context);

	public void toString(StringBuilder sb);
}
