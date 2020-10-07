package org.futurepages.jersey.core;

import org.futurepages.core.config.Apps;
import org.futurepages.jersey.filters.AppExceptionMapper;
import org.futurepages.jersey.filters.CORSFilter;
import org.futurepages.jersey.filters.JsonWithGsonMapper;
import org.futurepages.menta.core.formatter.FormatterManager;
import org.futurepages.menta.core.i18n.LocaleManager;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

public class JerseyAppManager extends ResourceConfig {

	public JerseyAppManager() {
		property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

		packages("www.app");

		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/app");
		register(JspMvcFeature.class);
		register(GlobalFilters.App.class);

		register(AppExceptionMapper.class);
		register(CORSFilter.class);
		register(JsonWithGsonMapper.class);

		if(Apps.isTrue("DISABLE_CONTROLLER")){
			LocaleManager.addDefault();
			FormatterManager.loadDefaults();
		}
	}
}

