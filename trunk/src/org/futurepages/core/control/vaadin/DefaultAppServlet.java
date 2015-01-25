package org.futurepages.core.control.vaadin;

import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import org.futurepages.core.exception.DefaultExceptionLogger;

import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class DefaultAppServlet extends VaadinServlet {

    @Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        String customSessionListenerClassPath = getServletConfig().getInitParameter("session.listener.path");

        Class customSessionInitListenerClass = null;
        if(customSessionListenerClassPath!=null){
            try {
                customSessionInitListenerClass = Class.forName(customSessionListenerClassPath);
                 if(!SessionInitListener.class.isAssignableFrom(customSessionInitListenerClass)){
                     customSessionInitListenerClass = null;
                    throw new Exception("SessionInitListener not registered. It should implements com.vaadin.server.SessionInitListener");
                }
            } catch (Exception e) {
                DefaultExceptionLogger.getInstance().execute(e);
            }
        }
        if(customSessionInitListenerClass!=null){
            try {
                getService().addSessionInitListener((SessionInitListener) customSessionInitListenerClass.newInstance());
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }else{
            getService().addSessionInitListener(new DefaultSessionInitListener());
        }
    }
}