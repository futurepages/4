package org.futurepages.menta.core.input;

import javax.servlet.http.HttpServletRequest;

public class PrettyGlobalURLRequestInput extends RequestInput {
	
	public PrettyGlobalURLRequestInput(HttpServletRequest req) {
		
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
		
		if (s.length > 1) {
			
			int total = s.length - 1;
			
			for(int i=0;i<total;i++) {
				map.put(String.valueOf(i), s[i + 1]);
				keys.add(String.valueOf(i));
			}
		}
	}
}