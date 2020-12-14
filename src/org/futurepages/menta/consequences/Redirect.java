package org.futurepages.menta.consequences;

import org.futurepages.core.config.Apps;
import org.futurepages.core.path.Paths;
import org.futurepages.menta.core.action.Action;
import org.futurepages.menta.core.consequence.Consequence;
import org.futurepages.menta.core.output.Output;
import org.futurepages.menta.exceptions.ConsequenceException;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Iterator;

import static org.futurepages.util.The.concat;

/**
 * A redirect consequence that has the following features:
 *
 * - Paths starting with "//" are relative to the webserver root.
 * - Paths containing "://" are absolute paths.
 * - All other paths (starting with "/" or not) are related to the context path.
 * - Supports dynamic URLs and dynamic parameters, with the dynamic values will come from action output.
 * - Dynamic parameters will be encoded. Static parameters (hardcoded in the URL) are assumed to be encoded.
 */
public class Redirect implements Consequence {

	public static final String REDIR_URL = Action.REDIR_URL;
	private String url = null;
	private boolean appendOutput = false;
	private boolean fromOutput = false;

	/** True if the URL is to be obtained from the {@link Output}. */
	public Redirect(String url) {
		this.url = url;
	}

	public Redirect(String url, boolean appendOutput) {
		this.url = url;
		this.appendOutput = appendOutput;
	}

	public Redirect() {
		this.fromOutput = true;
	}

	/**
	 * The redirect url is obtained from the action output.
	 *
	 * The URL query parameters are obtained from the {@link Output} object for
	 * the {@link Action}. All the parameters are appended to the URL query
	 * string.
	 *
	 * @param appendOutput is true if this redirect is to use dynamic parameters.
	 */
	public Redirect(boolean appendOutput) {
		this.fromOutput = true;
		this.appendOutput = appendOutput;
	}

	/**
	 * Execute the redirect consequence. The URL to redirect to is built up as
	 * necessary, depending on the type of redirection that is required - see the
	 * constructors for this class for the various flavours of redirections that
	 * are supported.
	 */
	@Override
	public void execute(Action act, HttpServletRequest req, HttpServletResponse res) throws ConsequenceException {
		try {
			Output output = act != null ? act.getOutput() : null;
			String theURL;
			if (fromOutput && output != null) {
				theURL = (String) output.getValue(REDIR_URL);
			} else {
				theURL = this.url;
			}

			theURL = fixedURLByType(theURL, req);

			// Check whether the redirect already have a query string (some parameters)
			URI uri = new URI(theURL);

			String urlPath = pathWithPrettyParams(uri.getPath(), output);
			String urlQuery = theURL.contains("?")? theURL.substring(theURL.indexOf("?")+1) : null;

			StringBuilder urlToRedir = builBasicUrlToRedir(uri);

			urlToRedir.append(urlPath);

			String querySeparator = "?";
			if(!Is.empty(urlQuery)) {
				urlToRedir.append("?").append(urlQuery);
				querySeparator = "&";
			}

			if (appendOutput && output != null) {
				appendOutputToURL(urlToRedir, output, querySeparator);
			}
			if (!Is.empty(uri.getFragment())) {
				urlToRedir.append("#").append(uri.getFragment());
			}
			res.sendRedirect(urlToRedir.toString());
		} catch (Exception e) {
			throw new ConsequenceException(e);
		}
	}

	private String pathWithPrettyParams(String urlPath, Output output) {
		if (output != null && output.getValue(Action.PRETTY_URL_PARAMS) != null) {
			Object[] prettyParams = (Object[]) output.getValue(Action.PRETTY_URL_PARAMS);
			for (int i = 0; i < prettyParams.length; i++) {
				prettyParams[i] = EncodingUtil.encodeUrl(prettyParams[i].toString());
			}
			String fstSlash = (urlPath.charAt(urlPath.length()-1)) != '/' ? "/" : "";
			return concat(urlPath, fstSlash, The.implodedArray(prettyParams, "/", null));
		}
		return urlPath;
	}

	private String fixedURLByType(String theURL, HttpServletRequest req) throws ConsequenceException {
			if (theURL == null || theURL.length() == 0) {
				throw new ConsequenceException("Missing url for redirect!");
			}

			if (theURL.indexOf("://") > 0) {
				return theURL;
			} else if (theURL.startsWith("//")) {
				// url relative to the ROOT of the web server...
				return theURL.substring(1, theURL.length());
			} else {
				// url relative to the context path...
				return concat(Paths.getInstance().getContext(req), (!theURL.startsWith("/") ? "/" : ""), theURL);
			}
	}

	private void appendOutputToURL(StringBuilder urlToRedir, Output output, String querySeparator) {
		StringBuilder queryFromOutputSB = new StringBuilder();
		Iterator<String> iter = output.keys();
		while (iter.hasNext()) {
			String key = (String) iter.next();

			// Skip the reserved keys from framework.
			if (key.equals(Action.REDIR_URL)
					|| key.equals(Action.HEAD_TITLE)
					|| key.equals(Action.MODULE_ID_KEY)
					|| key.equals(Action.PRETTY_URL_PARAMS)) {
				continue;
			}

			Object value = output.getValue(key);
			// Skip null values.
			if (value == null) {
				continue;
			}

			if (queryFromOutputSB.length() > 0) {
				queryFromOutputSB.append('&');
			}

			queryFromOutputSB.append(key);
			queryFromOutputSB.append("=");
			queryFromOutputSB.append(EncodingUtil.encodeUrl(value.toString()));
		}

		if (queryFromOutputSB.length() > 0) {
			urlToRedir.append(querySeparator).append(queryFromOutputSB);
		}
	}

	private StringBuilder builBasicUrlToRedir(URI uri) {
		StringBuilder urlToRedir = new StringBuilder();
		if(uri.getHost()!=null) {
				urlToRedir.append(concat(Apps.get("DEFAULT_SCHEME"),"://",uri.getHost(),(uri.getPort()!=80 && uri.getPort()!=443 && uri.getPort()!=-1 ? ":"+uri.getPort() : "")));
		}
		return urlToRedir;
	}

	/**
	 * @return a string representation of the redirect.
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Redirect to ");
		if (url != null) {
			s.append(url);
		} else {
			s.append(" url from output");
		}
		if (appendOutput) {
			s.append(" appendOutput");
		}
		return s.toString();
	}
}