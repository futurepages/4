package org.futurepages.jersey.filters;

import org.futurepages.jersey.actions.JerseyAction;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

@PreMatching
public class PreMatchingRequestFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext crc){
		System.out.print(">>> MyPreMatchingRequestFilter: ");
		System.out.println(JerseyAction.invoked());
	}
}
