package org.futurepages.jersey.core;

import org.futurepages.core.exception.AppLogger;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

public class GlobalFilters {

	public static class Api implements DynamicFeature {
		@Override
		public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
			try {
				GlobalActionsManager globals = (GlobalActionsManager) Class.forName("www.Globals").newInstance();
				globals.api(resourceInfo, featureContext);
			} catch (Exception e) {
				System.out.println("We couldn't find www.Globals as expected!");
				AppLogger.getInstance().execute(e);
			}
		}
	}

	public static class App implements DynamicFeature {
		@Override
		public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
			try {
				GlobalActionsManager globals = (GlobalActionsManager) Class.forName("www.Globals").newInstance();
				globals.app(resourceInfo, featureContext);
			} catch (Exception e) {
				System.out.println("We couldn't find www.Globals as expected!");
				AppLogger.getInstance().execute(e);
			}
		}
	}

	public static class Sse implements DynamicFeature {
		@Override
		public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
			try {
				GlobalActionsManager globals = (GlobalActionsManager) Class.forName("www.Globals").newInstance();
				globals.sse(resourceInfo, featureContext);
			} catch (Exception e) {
				System.out.println("We couldn't find www.Globals as expected!");
				AppLogger.getInstance().execute(e);
			}
		}
	}
}
