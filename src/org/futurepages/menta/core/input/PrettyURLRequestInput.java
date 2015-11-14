package org.futurepages.menta.core.input;

import org.futurepages.util.EncodingUtil;

import javax.servlet.http.HttpServletRequest;

public class PrettyURLRequestInput extends RequestInput {
	
	public PrettyURLRequestInput(HttpServletRequest req) {
		
		// super will process the parameters as usual...
		
		super(req);
		
		// get the parameters from the request...
		String context = req.getContextPath();
		String uri = req.getRequestURI().toString();

		// remove the context from the uri, if present
		if (context.length() > 0 && uri.indexOf(context) == 0) {
			uri = uri.substring(context.length());
		}
		
		uri = uri.replace("/?", "/ ?");
		uri = uri.replace("//", "/ /");
		uri = uri.replace("//", "/ /");
		
		// cut the first '/'
		if (uri.startsWith("/") && uri.length() > 1) {
			uri = uri.substring(1);
		}
		
		// cut last '/'
		if (uri.endsWith("/") && uri.length() > 1) {
			uri = uri.substring(0, uri.length() - 1);
		}
		
		String[] s = uri.split("/");
		
		if (s.length > 2) {
			int total = s.length - 2;
			for(int i=0;i<total;i++) {
				String theKey = String.valueOf(i);
				map.put(theKey, EncodingUtil.decodeUrl(s[i + 2]));
				keys.add(theKey);
			}
		}
	}
}