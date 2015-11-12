package org.futurepages.util.template.simpletemplate.template.exceptions;


import org.futurepages.util.The;

/**
 *
 * @author thiago
 */
public class TemplateException extends Exception {

	private static String concat(String name, String msg, int line, String error) {
		StringBuilder sb = new StringBuilder();
		
		if (name != null && !name.trim().isEmpty()) {
			sb.append("[").append(name).append("]::");
		}
		
		sb.append(msg).append(":").append(line);

		if (error != null && error.length() > 0) {
			sb.append("\n").append(error);
		}
		
		return sb.toString();
	}
	
	private int line;
	private String error;
	private String name;

	private String htmlMessageCache;

	public TemplateException(String msg, int line, String error) {
		super(msg);
		this.line = line;
		this.error = error;
	}

	public TemplateException(String name, String msg, int line, String error) {
		super(msg);
		this.line = line;
		this.error = error;
		this.name = name;
	}

	public TemplateException(String msg, int line) {
		this(msg, line, null);
	}

	public String getName() {
		return name;
	}

	public int getLine() {
		return line;
	}

	public String getError() {
		return error;
	}
	
	public String getMsg() {
		return super.getMessage();
	}
	
	@Override
	public String getMessage() {
		return concat(name, super.getMessage(), line, error);
	}
	
	public String getHtmlMessage() {
		
		if (htmlMessageCache == null) {
			String msg = getMessage().trim();
			StringBuilder sb = new StringBuilder();

			for (int i = 0, len = msg.length(); i < len; i++) {
				char ch = msg.charAt(i);

				switch (ch) {
					case '<':
						sb.append("&lt;");
						break;
					case '>':
						sb.append("&gt;");
						break;
					default:
						sb.append(ch);
				}
			}

			htmlMessageCache = sb.toString();
		}
		
		return The.concat("<pre>", htmlMessageCache, "</pre>");
	}
}
