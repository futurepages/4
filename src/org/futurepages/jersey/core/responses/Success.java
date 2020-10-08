package org.futurepages.jersey.core.responses;

public class Success {

	private boolean success;
	private Object obj;

	public Success(Object obj) {
		success = true;
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}