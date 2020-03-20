package org.futurepages.jersey.filters;

import org.futurepages.jersey.actions.JerseyAction;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

public class RequestFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext crc) {
		System.out.print(">>> MyRequestFilter");
		System.out.println(JerseyAction.invoked());
	}
}
