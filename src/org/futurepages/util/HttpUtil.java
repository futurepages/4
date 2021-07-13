package org.futurepages.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.http.HttpServletResponse;

public class HttpUtil {

	public static void disableCache(HttpServletResponse res) {
		res.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		res.setHeader("Pragma", "no-cache"); // HTTP 1.0
		res.setDateHeader("Expires", 0); // prevents caching at the proxy server
	}

	public static String getURLContent(String url){
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(url)
					.build();
			Response response = client.newCall(request).execute();
			assert response.body() != null;
			String str = response.body().string();

			// Close OkHttpClient according to:
			// https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
			response.body().close();
			response.close();
			client.dispatcher().executorService().shutdown();
			client.connectionPool().evictAll();
			return str;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Response getURLResponse(String url){
		OkHttpClient client = new OkHttpClient();
		try {
			Request request = new Request.Builder()
					.url(url)
					.build();
			return client.newCall(request).execute();

			// Close OkHttpClient according to:
			// https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			client.dispatcher().executorService().shutdown();
			client.connectionPool().evictAll();
		}
	}
}