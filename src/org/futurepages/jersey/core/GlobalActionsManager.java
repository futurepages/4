package org.futurepages.jersey.core;

import org.futurepages.jersey.actions.JerseyAction;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class GlobalActionsManager implements Filter{

	protected static JerseyAction action(){
		return JerseyAction.invoked();
	}

	public void api(ResourceInfo resourceInfo, FeatureContext context){}

	public void app(ResourceInfo resourceInfo, FeatureContext context){}

	public void sse(ResourceInfo resourceInfo, FeatureContext context){}

	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {}

	@Override
	public void destroy() {}
}
