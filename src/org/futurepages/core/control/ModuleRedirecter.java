package org.futurepages.core.control;

import org.futurepages.consequences.Redirect;
import org.futurepages.core.config.Params;
import org.futurepages.core.exception.DefaultExceptionLogger;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The central controller. The actions are intercepted and
 * executed by this controller. The controller is also responsable for creating
 * and starting the ApplicationManager.
 *
 * @author Sergio Oliveira
 * @author Rubem Azenha (rubem.azenha@gmail.com)
 * @author Leandro Santana Pereira
 * @author Danilo Batista
 */
public class ModuleRedirecter extends HttpServlet {

	private static String targetModule = null;

	/**
	 * Initialize the Controller, creating and starting the ApplicationManager.
	 */
	@Override
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		try     {
			if(conf.getInitParameter("targetModule")==null){
				throw new Exception("Who is the target module for "+conf.getServletName()+"?");
			}
			targetModule = conf.getInitParameter("targetModule");

		} catch (Exception ex) {
			DefaultExceptionLogger.getInstance().execute(ex, null, null, true);
		}

	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String url = req.getRequestURL().toString();
		try {
			String urlInit = The.concat(req.getScheme(), "://", req.getHeader("host"),req.getContextPath(),(Params.get("PRETTY_URL").equals("true")?"":"/"+Params.MODULES_PATH));

			String newUrl = req.getRequestURL().toString().replace(urlInit, "");
			String[] urlParts = newUrl.split("/");

			StringBuilder newUrlSB = new StringBuilder();

			newUrlSB.append("/").append(targetModule);
			for(int i = 2; i < urlParts.length ; i++){
				newUrlSB.append("/").append(urlParts[i]);
			}
			newUrlSB.append(!Is.empty(req.getQueryString())?"?"+req.getQueryString():"");


			(new Redirect(newUrlSB.toString())).execute(null, req, res);
		} catch (Exception ex) {
			throw new ServletException("Impossible to redirect '"+url+"' to '"+targetModule+"' module");
		}
	}
}