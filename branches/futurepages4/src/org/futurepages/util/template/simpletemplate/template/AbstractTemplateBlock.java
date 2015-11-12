package org.futurepages.util.template.simpletemplate.template;

import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public abstract class AbstractTemplateBlock {

	private AbstractTemplateBlock nextInner;
	private AbstractTemplateBlock nextInnerElse;
	private AbstractTemplateBlock next;

	public AbstractTemplateBlock() {
	}

	public AbstractTemplateBlock getNextInner() {
		return nextInner;
	}

	public void setNextInner(AbstractTemplateBlock nextInner) {
		this.nextInner = nextInner;
	}

	public void setNextInnerElse(AbstractTemplateBlock nextInnerElse) {
		this.nextInnerElse = nextInnerElse;
	}

	public AbstractTemplateBlock getNextInnerElse()  {
		return nextInnerElse;
	}

	public void setNext(AbstractTemplateBlock next) {
		this.next = next;
	}

	public AbstractTemplateBlock getNext() {
		return next;
	}

	public void putInEnd(AbstractTemplateBlock last) {
		AbstractTemplateBlock item = this;

		while (item.getNext() != null) {
			item = item.getNext();
		}

		item.setNext(last);
	}

	public void append(AbstractTemplateBlock other) {
		if (this.getNextInner() != null) {
			this.getNextInner().putInEnd(other);
		} else {
			this.setNextInner(other);
		}
	}

	public void appendToElse(AbstractTemplateBlock other) {
		if (this.getNextInnerElse() != null) {
			this.getNextInnerElse().putInEnd(other);
		} else {
			this.setNextInnerElse(other);
		}
	}

	@Override
	public abstract String toString();

	public abstract void toString(StringBuilder sb);

	public abstract void eval(ContextTemplateTag context, TemplateWriter sb);
}
