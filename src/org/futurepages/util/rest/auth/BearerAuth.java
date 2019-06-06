package org.futurepages.util.rest.auth;

import java.net.HttpURLConnection;

public class BearerAuth implements HttpAuthentication {

	private final String accessToken;

	public BearerAuth(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public String getToken(){
		return accessToken;
	}

	public void authenticate(HttpURLConnection connection) {
		connection.addRequestProperty("Authorization", "Bearer " + accessToken);
	}
}
