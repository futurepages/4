package org.futurepages.jersey.core;

import org.futurepages.jersey.filters.CORSFilter;
import org.futurepages.jersey.filters.JsonWithGsonMapper;
import org.futurepages.jersey.filters.PreMatchingRequestFilter;
import org.futurepages.jersey.filters.RequestFilter;
import org.futurepages.jersey.filters.ResponseFilter;
import org.futurepages.jersey.filters.WriterInterceptor;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.futurepages.jersey.filters.AppExceptionMapper;

public class JerseyApiManager extends ResourceConfig {

	public JerseyApiManager() {
		property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

		packages("www.api");

		register(AppExceptionMapper.class);
		register(CORSFilter.class);
		register(JsonWithGsonMapper.class);

		// invocation chain...
		register(PreMatchingRequestFilter.class);
		register(RequestFilter.class);
		register(ResponseFilter.class);
		register(WriterInterceptor.class);
	}
}

