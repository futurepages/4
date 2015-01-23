package org.futurepages.util.template.simpletemplate.expressions.tree;

import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Identifier implements Token {
//	private static final Pattern splitter = Pattern.compile("\\s*\\.\\s*");
	
	private String id;
//	private String [] keys;
	
	public Identifier() {
	}

	public Identifier(String id) {
		setId(id);
	}

	public String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
//		keys = splitter.split(id);
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		return context.getValue(id);
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(id);
	}
}
