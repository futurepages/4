package org.futurepages.jersey.core;

import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.filters.HibernateFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class JerseyController extends ServletContainer {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) {
		try{
			super.service(request, response);
			doService(request, response);
		} catch (Exception ex){
			AppLogger.getInstance().execute(ex,request);
		}finally {
			try{
				HibernateFilter.finallly();
			}catch (Exception e){
				AppLogger.getInstance().execute(e,request);
			}
		}
	}

	private void doService(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		fixEncoding(request,response );
	}

	private static void fixEncoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		if(Apps.isTrue("NEW_JERSEY_MODE")){
			String charset = Apps.get("PAGE_ENCODING");
			if (charset.equals("UTF-8")) {
				request.setCharacterEncoding(charset);
			}
			response.setCharacterEncoding(charset);
		}
		// se algum dia precisar, para aplicativos mobile, descomentar... (by Dimmy)
		// response.setHeader("Access-Control-Allow-Origin","*");
	}
}