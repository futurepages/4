package org.futurepages.jersey.filters;

import org.futurepages.jersey.actions.JerseyAction;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

public class WriterInterceptor implements javax.ws.rs.ext.WriterInterceptor {
	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		System.out.print(">>> MyWriterInterceptor: ");
		System.out.println(JerseyAction.invoked());
		context.proceed();
	}
}