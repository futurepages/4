package org.futurepages.jersey.filters;

import org.futurepages.jersey.actions.JerseyAction;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class ResponseFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		System.out.print(">>> MyResponseFilter: ");
		System.out.println(JerseyAction.invoked());
	}
}
