package org.futurepages.menta.core.template;

import org.futurepages.menta.actions.NullAction;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.exceptions.TemplateException;
import org.futurepages.menta.filters.ExceptionFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe abstrata base para TemplateServlets. Possui uma implementacao padrao,
 * que e JspTemplateServlet, porem podem ser feitas outras, como
 * FreemarkerTemplateServlet ou VelocityTemplateServlet
 *
 * @author Davi Luan Carneiro
 */
public class TemplateServlet extends HttpServlet {

	/**
	 * Para acessar o initParam do web.xml
	 */
	protected static final String TEMPLATE_MANAGER_ATTR = "TemplateManager";
	public static final String PAGE_ATTR = "_page_fpg";
	public static final String CURRENT_PATH = "_current_path_for_template";

	private static AbstractTemplateManager templateManager = null;

	/**
	 * O TemplateManager sera carregado no startup do container
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		try{
			templateManager = createTemplateManager();
		}catch(Exception ex){
			ExceptionFilter.Logger.getInstance().execute(ex, null, null, true);
		}
	}

	private AbstractTemplateManager createTemplateManager() {
		if (templateManager != null) {
			return templateManager;
		}
		AbstractTemplateManager manager;
		try {
			String nameClassTemplateManager = getInitParameter(TEMPLATE_MANAGER_ATTR);
			if (nameClassTemplateManager == null || "".equals(nameClassTemplateManager)) {
				nameClassTemplateManager = TEMPLATE_MANAGER_ATTR;
			}
			Class classTemplateManager = TemplateManager.class;
			manager = (AbstractTemplateManager) classTemplateManager.newInstance();
			if (manager == null) {
				throw new TemplateException("TemplateManager not found");
			}
			manager.configurePages();
			return manager;
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	public static AbstractTemplateManager getTemplateManager() {
		return templateManager;
	}

	public static void setTemplateManager(AbstractTemplateManager manager) {
		templateManager = manager;
	}

	public static String extractPagePath(HttpServletRequest request) {
		String servletPath = request.getServletPath();
		return servletPath.substring(0, servletPath.lastIndexOf("."));
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String path = extractPagePath(request);

			if(!path.startsWith("/init/") && request.getAttribute(Forward.ACTION_REQUEST)==null){
				throw new TemplateException("Page can't be retrieved when action is null.");
			}else if(request.getAttribute(Forward.ACTION_REQUEST)==null){
				Controller.fixEncoding(request,response);
				
				//Creating Null Action - without breaking.
				new NullAction(request,response);
			}
			processTemplate(path, createTemplateManager(), request, response, getServletContext());
		} catch (Exception ex) {
			throw new ServletException(ex);
			// antes a linha de cima era assim.... (removi pra evitar logs repetidos)
//			ExceptionFilter.Logger.getInstance().execute(ex, null, request, true);
		}
	}

	public void processTemplate(String path, AbstractTemplateManager manager, HttpServletRequest request, HttpServletResponse response, ServletContext application) {
		Page page = manager.getPageForPath(path);
		if (page == null) {
			throw new TemplateException("'"+path + "' not found!");
		}

		try {
			request.setAttribute(PAGE_ATTR, page);
			request.setAttribute(CURRENT_PATH, path);
			application.getRequestDispatcher("/" + page.getView()).forward(request, response);
		} catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException {
		processRequest(request, response);
	}
}
