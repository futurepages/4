package org.futurepages.test;

/**
 * Nesta classe há a chamada de um pacote que temos que verificar se esta na lib do futurepages4
 */

import okhttp3.Response;
import org.futurepages.util.HttpUtil;
import org.futurepages.util.The;

public class TestingService {

	/**
	 *
	 * @param url
	 * @return
	 */
	public static Response getResponseFromURL(String url){
		return HttpUtil.getURLResponse(url);
	}
}
