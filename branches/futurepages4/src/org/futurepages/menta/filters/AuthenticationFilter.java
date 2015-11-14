package org.futurepages.menta.filters;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.actions.LoginAction;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.action.AsynchronousManager;
import org.futurepages.menta.core.action.AuthenticationFree;
import org.futurepages.menta.core.action.RedirectAfterLogin;
import org.futurepages.menta.core.context.Context;
import org.futurepages.menta.core.context.SessionContext;
import org.futurepages.menta.core.control.InvocationChain;
import org.futurepages.menta.core.filter.Filter;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A filter to handle user authentcation.
 * You should use this filter to protect your actions from unauthorized access.
 *
 * @author Sergio Oliveira
 */
public class AuthenticationFilter implements Filter {

	public static final String URL_KEY = "url";

	private String urlRedirect;
	private String nextVarName;
	private boolean definedUrlLogin;

	/**
	 * Creates a new authentication filter.
	 */
	public AuthenticationFilter() {
		urlRedirect = Apps.get("LOGIN_URL_REDIRECT");
		nextVarName = Apps.get("LOGIN_URL_REDIRECT_VAR_NAME");

		definedUrlLogin = !Is.empty(urlRedirect);
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Action action = chain.getAction();
		String innerAction = chain.getInnerAction();
		Context session = action.getSession();
		boolean shouldBypass = false;

		if (action instanceof AuthenticationFree) {
			if (!(action instanceof LoginAction)) {
				session.setAttribute(AuthenticationFilter.URL_KEY, null);
			}
			AuthenticationFree af = (AuthenticationFree) action;
			shouldBypass = af.bypassAuthentication(innerAction);
		}

		if (!shouldBypass) {
			Filter f = chain.getFilter(AuthenticationFreeMarkerFilter.class);
			if (f != null) {
				AuthenticationFreeMarkerFilter aff = (AuthenticationFreeMarkerFilter) f;
				shouldBypass = aff.bypassAuthentication(innerAction);
			}
		}

		// Devo "não passar" e não estou logado.
		// Equivale a:
		// Não (devo passar ou estou logado).
		if (!shouldBypass && !LoginAction.isLogged(session)) {
			boolean shouldRedirect = false;
			boolean isAjax = AsynchronousManager.isAjaxAction(chain);
			boolean isDyn = AsynchronousManager.isDynAction(chain);

			if (definedUrlLogin && (!isAjax && !isDyn)) {
				action.getOutput().setValue(
					Action.REDIR_URL,
					The.concat(getDomain(action.getRequest()), getURIRedirect(action.getRequest()))
				);

				return REDIR;
			}

			if ((action instanceof RedirectAfterLogin) && !(AsynchronousManager.isAsynchronousAction(chain))) {
				RedirectAfterLogin ral = (RedirectAfterLogin) action;
				shouldRedirect = ral.shouldRedirect(innerAction);
			}

			//maker ??
			if (!shouldRedirect) {
				Filter f = chain.getFilter(ShouldRedirectFilter.class);
				if (f != null) {
					ShouldRedirectFilter ramf = (ShouldRedirectFilter) f;

					shouldRedirect = ramf.shouldRedirect(innerAction);
				}
			}

			if (shouldRedirect) {
				HttpServletRequest req = ((SessionContext) session).getRequest();
				HttpSession ses = ((SessionContext) session).getSession();
				setCallbackUrl(ses, req);
			}

			if (isAjax) {
				return AJAX_DENIED;
			}

			if (isDyn) {
				return DYN_LOGIN;
			}

			action.getOutput().setValue("login_needed", true);

			return LOGIN;
		}

		return chain.invoke();
	}

	private String getDomain(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();

		sb.append(req.getScheme()).append("://").append(req.getServerName());

		if (!Is.empty(req.getServerPort())) {
			sb.append(":").append(req.getServerPort());
		}

		return sb.toString();
	}

	private String getURIRedirect(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();

		sb.append(req.getContextPath()).append(urlRedirect).append("?").append(nextVarName).append("=").append(EncodingUtil.encodeUrl(req.getRequestURI()));

		if (!Is.empty(req.getQueryString())) {
			sb.append(EncodingUtil.encodeUrl("?" + req.getQueryString()));
		}
		return sb.toString();
	}


	/**
	 * Sets a callback url for a redirection after the login.
	 * This method is called by the authentication tag to set a url for the redirection.
	 * You should not call this method.
	 *
	 * @param session The HttpSession where to put the URL.
	 * @param req     The HttpServletRequest from where to get the URL.
	 */
	public static void setCallbackUrl(HttpSession session, HttpServletRequest req) {
		StringBuffer url = req.getRequestURL();
		String query = req.getQueryString();
		if (query != null) {
			url.append('?');
			url.append(query);
		}
		session.setAttribute(URL_KEY, url.toString());
	}

	@Override
	public void destroy() {
		this.urlRedirect = null;
		this.nextVarName = null;
	}
}