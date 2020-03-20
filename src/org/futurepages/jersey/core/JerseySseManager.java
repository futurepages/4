package org.futurepages.jersey.core;

import org.futurepages.jersey.filters.AppExceptionMapper;
import org.futurepages.jersey.filters.CORSFilter;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseySseManager extends ResourceConfig {

	public JerseySseManager() {
		property(SseFeature.DISABLE_SSE, false);
		property(SseFeature.DISABLE_SSE_CLIENT, false);
		property(SseFeature.DISABLE_SSE_SERVER, false);
		property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

		packages("www.sse");

		register(SseFeature.class);

		register(AppExceptionMapper.class);
		register(CORSFilter.class);
	}
}

