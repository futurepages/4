package org.futurepages.menta.actions;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.menta.consequences.Forward;
import org.futurepages.menta.core.context.ApplicationContext;
import org.futurepages.menta.core.context.CookieContext;
import org.futurepages.menta.core.context.MapContext;
import org.futurepages.menta.core.context.SessionContext;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.futurepages.menta.core.input.PrettyURLRequestInput;
import org.futurepages.menta.core.output.ResponseOutput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action instanciada quando não há action no request.
 * @author leandro
 */
public class NullAction extends FreeAction {
	
	public NullAction(HttpServletRequest req , HttpServletResponse res){
		this.setInput(new PrettyURLRequestInput(req));
		this.setOutput(new ResponseOutput(res));
		this.setSession(new SessionContext(req, res));
		this.setApplication(new ApplicationContext(req.getServletContext()));
		this.setCookies(new CookieContext(req, res));
		this.setLocale(LocaleManager.getLocale(req));
		this.setCallback(new MapContext());
		req.setAttribute(Forward.ACTION_REQUEST, this);

		Controller.setThredLocalChain(new InvocationChain(this.getClass().getName(), this));
	}
}
