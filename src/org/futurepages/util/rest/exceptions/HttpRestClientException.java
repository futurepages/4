package org.futurepages.util.rest.exceptions;

public class HttpRestClientException extends Exception {

	private final int responseCode;

	public HttpRestClientException(int responseCode, String msg) {
		super(msg);
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

}
