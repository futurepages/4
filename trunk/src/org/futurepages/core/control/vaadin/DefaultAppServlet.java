package org.futurepages.core.control.vaadin;

import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.Is;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class DefaultAppServlet extends VaadinServlet {


	@Override
	protected final void servletInitialized() throws ServletException {
		super.servletInitialized();
		String customSessionListenerClassPath = getServletConfig().getInitParameter("session.listener.path");

		if (customSessionListenerClassPath == null || !customSessionListenerClassPath.equals("none")) {
			Class customSessionInitListenerClass = null;
			if (customSessionListenerClassPath != null) {
				try {
					customSessionInitListenerClass = Class.forName(customSessionListenerClassPath);
					if (!SessionInitListener.class.isAssignableFrom(customSessionInitListenerClass)) {
						customSessionInitListenerClass = null;
						throw new Exception("SessionInitListener not registered. It should implements com.vaadin.server.SessionInitListener");
					}
				} catch (Exception e) {
					DefaultExceptionLogger.getInstance().execute(e);
				}
			}
			if (customSessionInitListenerClass != null) {
				try {
					getService().addSessionInitListener((SessionInitListener) customSessionInitListenerClass.newInstance());
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else {
				getService().addSessionInitListener(new DefaultSessionInitListener());
			}
		}
	}


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		String url = request.getRequestURL()+(!Is.empty(request.getQueryString())? "?" +request.getQueryString() :"");
//		System.out.println("BEGIN "+url+" >>>>>>>>>>>>>>>>>>");
//		long inicio = System.currentTimeMillis();
		try {
			Dao.getInstance().beginTransaction();
			super.service(request, response);
			if(Dao.getInstance().isTransactionActive()){
				Dao.getInstance().commitTransaction();
			}
		} catch (Throwable throwable) {
			if(Dao.getInstance().isTransactionActive()){
				Dao.getInstance().rollBackTransaction();
			}
			throw new ServletException(throwable);
		} finally {
			Dao.getInstance().close();
		}
//		System.out.println((System.currentTimeMillis()-inicio)+"ms");
//		System.out.println("END   "+url+" <<<<<<<<<<<<<<<<<<");
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
