package org.futurepages.menta.core.template;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.actions.NullAction;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.context.ApplicationContext;
import org.futurepages.menta.core.context.CookieContext;
import org.futurepages.menta.core.context.MapContext;
import org.futurepages.menta.core.context.SessionContext;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.core.input.PrettyURLRequestInput;
import org.futurepages.menta.core.output.ResponseOutput;
import org.futurepages.menta.exceptions.TemplateException;

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
			AppLogger.getInstance().execute(ex);
			throw new ServletException(ex);
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
			Class classTemplateManager = Class.forName(nameClassTemplateManager);
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
				Action action = new NullAction();
				action.setInput(new PrettyURLRequestInput(request));
				action.setOutput(new ResponseOutput(response));
				action.setSession(new SessionContext(request, response));
				action.setApplication(new ApplicationContext(this.getServletContext()));
				action.setCookies(new CookieContext(request, response));
				action.setLocale(LocaleManager.getLocale(request));
				action.setCallback(new MapContext());
				request.setAttribute(Forward.ACTION_REQUEST, action);
			}
			processTemplate(path, createTemplateManager(), request, response, getServletContext());
		} catch (Exception ex) {
			AppLogger.getInstance().execute(ex);
			throw new ServletException(ex);
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
