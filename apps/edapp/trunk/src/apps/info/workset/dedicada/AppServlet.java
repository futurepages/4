package apps.info.workset.dedicada;

import com.vaadin.server.VaadinServlet;

import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class AppServlet extends VaadinServlet {

    @Override
    protected final void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener(new EDSessionInitListener());
    }
}