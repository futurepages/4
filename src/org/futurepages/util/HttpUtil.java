package org.futurepages.util;

import javax.servlet.http.HttpServletResponse;

public class HttpUtil {

	public static void disableCache(HttpServletResponse res) {
		res.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		res.setHeader("Pragma", "no-cache"); // HTTP 1.0
		res.setDateHeader("Expires", 0); // prevents caching at the proxy server
	}

}