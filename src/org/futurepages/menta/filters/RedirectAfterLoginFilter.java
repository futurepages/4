package org.futurepages.menta.filters;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.annotations.RedirectAfterLogin;
import org.futurepages.menta.consequences.Redirect;
import org.futurepages.menta.core.ApplicationManager;
import org.futurepages.menta.core.action.AbstractAction;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.context.Context;
import org.futurepages.menta.core.control.AbstractApplicationManager;
import org.futurepages.menta.core.control.ActionConfig;
import org.futurepages.menta.core.control.Controller;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.menta.core.output.Output;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.Is;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.futurepages.util.The.concat;

/**
 * A filter that implements the redirect after login mechanism.
 * Apply this filter to your Login action if you want it to perform a redict 
 * to the first page the user tried to access.
 *
 * @author Sergio Oliveira
 */
public class RedirectAfterLoginFilter implements Filter {

	private String urlRedirect;
	private String nextVarName;
	private boolean definedUrlLogin;
	private Pattern varNextMatch;
	
    /**
     * Creates a RedirectAfterLoginFilter.
     */
	public RedirectAfterLoginFilter() {
		this.urlRedirect = Apps.get("LOGIN_URL_REDIRECT");
		this.nextVarName = Apps.get("LOGIN_URL_REDIRECT_VAR_NAME");

		definedUrlLogin = !Is.empty(this.urlRedirect);

		if (definedUrlLogin) {
			varNextMatch = Pattern.compile(concat("(\\?|\\&)", nextVarName, "\\="));
		}
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		Context session = action.getSession();
		String result = chain.invoke();
		String callback = (String) session.getAttribute(AuthenticationFilter.URL_KEY);

		Controller control = Controller.getInstance();
		String[] parts = null;

		if (definedUrlLogin && (!result.equals(AbstractAction.ERROR) &&
		    (parts = getActionName(control, action.getRequest())) != null)) {
			Action ac = getAction(control, action.getRequest(), parts);

			if (ac != null) {
				String actionName = parts[0], innerAction = parts[1];
				Class<? extends Action> classAction = ac.getClass();

				RedirectAfterLogin classAnnotation = classAction.getAnnotation(RedirectAfterLogin.class);
				RedirectAfterLogin methodAnnotation = null;

				if (!Is.empty(innerAction)) {
					Method method = classAction.getDeclaredMethod(innerAction);
					methodAnnotation = method.getAnnotation(RedirectAfterLogin.class);
				} else {
					Method method = classAction.getMethod(Action.EXECUTE);
					methodAnnotation = method.getAnnotation(RedirectAfterLogin.class);
				}

				if (methodAnnotation == null) {
					if (classAnnotation != null) {
						if (!classAnnotation.negate()) {
							String next =  getRefererQueryString(action.getRequest());
							String ret = returnRedirect(action, next);

							if (ret != null) {
								return ret;
							}
						} else {
							return result;
						}
					}
				} else {
					if (!methodAnnotation.negate()) {
						String next = getRefererQueryString(action.getRequest());
						String ret = returnRedirect(action, next);

						if (ret != null) {
							return ret;
						}
					} else {
						return result;
					}
				}
			}
		}

		if (callback != null && !result.equals(AbstractAction.ERROR)) {
			Output output = action.getOutput();
			output.setValue(Redirect.REDIR_URL, callback);
			return REDIR;
		}

		if (!result.equals(AbstractAction.ERROR) && definedUrlLogin) {
			String next = getRefererQueryString(action.getRequest());
			String ret = returnRedirect(action, next);

			if (ret != null) {
				return ret;
			}
		}

		return result;
	}

	private String getRefererQueryString(HttpServletRequest req) {
		String referer = req.getHeader("referer");

		if (!Is.empty(referer)) {
			Matcher matcher = varNextMatch.matcher(req.getHeader("referer"));

			if (matcher.find()) {
				return EncodingUtil.decodeUrl(referer.substring(matcher.end()));
			}else{
				return !Is.empty(req.getParameter(nextVarName))? req.getParameter(nextVarName) : null;
			}
		}

		return null;
	}

	private String getDomain(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();

		sb.append(req.getScheme()).append("://").append(req.getServerName());

		if (!Is.empty(req.getServerPort())) {
			sb.append(":").append(req.getServerPort());
		}

		return sb.toString();
	}

	private String[] getActionName(Controller control, HttpServletRequest currentReq) {
		String nextURI = getRefererQueryString(currentReq);

		if (!Is.empty(nextURI)) {
			int queryInit = nextURI.indexOf('?');

			if (queryInit >= 1) {
				nextURI = nextURI.substring(0, queryInit);
			} else if (queryInit == 0) {
				return null;
			}

			return control.getActionUrlParts(currentReq.getContextPath(), nextURI);
		}

		return null;
	}

	private Action getAction(Controller control, HttpServletRequest currentReq, String[] parts) {
		String nextURI = getRefererQueryString(currentReq);

		if (!Is.empty(nextURI)) {
			String actionName = parts[0];
			String innerAction = parts[1];

			ApplicationManager appManager = control.getAppManager();

			ActionConfig ac = null;

			if (innerAction != null) {
				ac = appManager.getActionConfig(actionName, innerAction);
			}

			if (ac == null) {
				ac = appManager.getActionConfig(actionName);
			}

			if (ac == null) {
				ac = AbstractApplicationManager.getDefaultAction();
			}

			return ac.getAction();
		}

		return null;
	}

	private String returnRedirect(Action action, String next) {
		if (!Is.empty(next)) {
			action.getOutput().setValue(
				Action.REDIR_URL,
				concat(getDomain(action.getRequest()), next)
			);

			return REDIR;
		}

		return null;
	}
    
	@Override
    public void destroy() { }
}
		