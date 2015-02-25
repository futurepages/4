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
		try {
			// TODO Find where to put @Transactional
			// TODO MultiTransactional - how?
			Dao.getInstance().beginTransaction();
//			System.out.println("TRANSACTION START THREAD#"+Thread.currentThread().getId());
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
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
