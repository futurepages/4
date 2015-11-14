package org.futurepages.util.templatizer.template;

import java.io.Serializable;
import org.futurepages.util.templatizer.expressions.primitivehandle.Const;

/**
 *
 * @author thiago
 */
public class TemplateWriter implements Serializable {

	private StringBuilder sb;
	private boolean printNull = false;;

	public TemplateWriter() {
		sb = new StringBuilder();
	}

	public TemplateWriter(int capacity) {
		sb = new StringBuilder(capacity);
	}

	public TemplateWriter(String str) {
		sb = new StringBuilder(str);
	}

	public TemplateWriter(CharSequence seq) {
		sb = new StringBuilder(seq);
	}

	public TemplateWriter(boolean printNull) {
		sb = new StringBuilder();
		this.printNull = printNull;
	}

	public TemplateWriter(String str, boolean printNull) {
		sb = new StringBuilder(str);
		this.printNull = printNull;
	}

	public TemplateWriter(CharSequence seq, boolean printNull) {
		sb = new StringBuilder(seq);
		this.printNull = printNull;
	}

	protected boolean isNull(Object obj) {
		return obj == null;
	}

	public TemplateWriter append(Object obj) {
		if (!isNull(obj) && obj != Const.NULL) {
			sb.append(obj);
		} else if (printNull) {
			sb.append("null");
		} else if (obj == Const.NULL) {
			sb.append("");
		}
		return this;
	}

	public TemplateWriter append(String str) {
		if (!isNull(str)) {
			sb.append(str);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	private TemplateWriter append(StringBuilder sb) {
		if (!isNull(sb)) {
			this.sb.append(sb);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(StringBuffer sb) {
		if (!isNull(sb)) {
			this.sb.append(sb);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(CharSequence s) {
		if (!isNull(s)) {
			sb.append(s);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(CharSequence s, int start, int end) {
		if (!isNull(s)) {
			sb.append(s, start, end);
		} else if (printNull) {
			sb.append("null");
		}

		return this;
	}

	public TemplateWriter append(char str[]) {
		if (!isNull(str)) {
			sb.append(str);
		} else if (printNull) {
			sb.append("null");
		}

		return this;
	}

	public TemplateWriter append(char str[], int offset, int len) {
		if (!isNull(str)) {
			sb.append(str, offset, len);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(boolean b) {
		if (!isNull(b)) {
			sb.append(b);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(char c) {
		if (!isNull(c)) {
			sb.append(c);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(int i) {
		if (!isNull(i)) {
			sb.append(i);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(long lng) {
		if (!isNull(lng)) {
			sb.append(lng);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(float f) {
		if (!isNull(f)) {
			sb.append(f);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWriter append(double d) {
		if (!isNull(d)) {
			sb.append(d);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public int length() {
		return sb.length();
	}

	public boolean isEmpty() {
		return sb.length() == 0;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
