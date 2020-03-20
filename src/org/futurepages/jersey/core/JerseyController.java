package org.futurepages.jersey.core;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.filters.HibernateFilter;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JerseyController extends ServletContainer {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) {
		try{
			super.service(request, response);
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
}