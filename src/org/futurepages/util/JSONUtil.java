package org.futurepages.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JSONUtil {

	public static JsonObject getObject(String url) {
		try {
			String jsonStr = HttpUtil.getURLContent(url);
			return new JsonParser().parse(jsonStr).getAsJsonObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonArray parseArray(String jsonStr) {
		return new JsonParser().parse(jsonStr).getAsJsonArray();
	}

	public static JsonArray getArray(String url) {
		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(url)
					.build();
			String jsonStr;
			Response response = client.newCall(request).execute();
			jsonStr = response.body().string();

			// Close OkHttpClient according to:
			// https://square.github.io/okhttp/3.x/okhttp/okhttp3/OkHttpClient.html
			client.dispatcher().executorService().shutdown();
			client.connectionPool().evictAll();
			return new JsonParser().parse(jsonStr).getAsJsonArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}