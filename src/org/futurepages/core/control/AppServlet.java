package org.futurepages.core.control;

import com.vaadin.server.VaadinServlet;
import org.futurepages.core.persistence.Dao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class AppServlet extends VaadinServlet {


	@Override
	protected final void servletInitialized() throws ServletException {
		super.servletInitialized();
	}


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		String url = request.getRequestURL()+(!Is.empty(request.getQueryString())? "?" +request.getQueryString() :"");
//		System.out.println("BEGIN "+url+" >>>>>>>>>>>>>>>>>>");
//		long inicio = System.currentTimeMillis();
		try {
			//TODO why beanTransaction necessary? A: org.hibernate.HibernateException: createQuery is not valid without active transaction
			Dao.getInstance().beginTransaction(); //TODO Find where to put @Transactional
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
