package org.futurepages.util.rest.auth;

import java.net.HttpURLConnection;

public interface HttpAuthentication {

	void authenticate(HttpURLConnection connection);
	String getToken();
}