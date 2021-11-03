package org.futurepages.test;

import okhttp3.Response;
import org.futurepages.util.HttpUtil;
import org.futurepages.util.The;

public class TestingService {

	public static String getContentResFromPath(String... path){
		return HttpUtil.getURLContent(DriverFactory.getPath(The.concat(path)));
	}

	public static Response getResponseFromPath(String... path){
		return HttpUtil.getURLResponse(DriverFactory.getPath(The.concat(path)));
	}

	public static Response getResponseFromURL(String url){
		return HttpUtil.getURLResponse(url);
	}
}
