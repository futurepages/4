package org.futurepages.jersey.core;

import org.futurepages.jersey.actions.JerseyAction;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class GlobalActionsManager {

	protected static JerseyAction action(){
		return JerseyAction.invoked();
	}

	public void api(ResourceInfo resourceInfo, FeatureContext context){}

	public void app(ResourceInfo resourceInfo, FeatureContext context){}

	public void sse(ResourceInfo resourceInfo, FeatureContext context){}
}
