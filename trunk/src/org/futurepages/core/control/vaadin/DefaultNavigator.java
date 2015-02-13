package org.futurepages.core.control.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.futurepages.core.config.Apps;

@SuppressWarnings("serial")
public abstract class DefaultNavigator extends Navigator {

    public DefaultNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);

        initGATrackerIfTheCase();
        initViewChangeListener();
        initViewProviders();

    }

	//TODO Make this two methods automated CoC like.
	protected abstract void initViewProviders();
	protected abstract void initViewChangeListener();


	//TODO add GoogleAnalytics Addon to fpg3 lib and migrate it.
    private void initGATrackerIfTheCase() {
	    String host = getUI().getPage().getLocation().getHost();
        String TRACKER_ID = Apps.get("GOOGLE_ANALYTICS_ID");
        if (TRACKER_ID != null && host.equals(Apps.get("PRODUCTION_HOST"))) {
//        GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker(TRACKER_ID, host);
//        // GoogleAnalyticsTracker is an extension add-on for UI so it is initialized by calling .extend(UI)
//        tracker.extend(UI.getCurrent());
        }
    }
}