package org.futurepages.util.iterator.string;


public class MatchedToken {

	private String matched;
	private String before;
	private String after;
	private int start;
	private int end;


	public MatchedToken(String matched, String before, String after) {
		super();
		this.matched = matched;
		this.before = before;
		this.after = after;
		this.start = before.length();
		this.end = before.length()+matched.length();
	}

	public String getBefore() {
		return before;
	}

	public int getEnd() {
		return end;
	}

	public int getStart() {
		return start;
	}
	

	public String getMatched() {
		return matched;
	}

	public String getAfter() {
		return after;
	}
	@Override
	public String toString() {
		return "before: "+before+"  token: "+matched;
	}
	}
