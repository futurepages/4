package org.futurepages.core.control.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.futurepages.core.config.Apps;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

@SuppressWarnings("serial")
public class DefaultNavigator extends Navigator {

    protected GoogleAnalyticsTracker gaTracker;
    protected ViewProvider errorViewProvider;
    private final DefaultViewItem HOME_ITEM_VIEW;

    private DefaultMenu menu;

    public DefaultNavigator(DefaultMenu menu, ComponentContainer container) {
        super(UI.getCurrent(), container);
        HOME_ITEM_VIEW = menu.getHomeItemView();
        this.menu = menu;
        initGATrackerIfTheCase();
        initViewChangeListener();
        initViewProviders();

    }

    private void initGATrackerIfTheCase() {
        String host = getUI().getPage().getLocation().getHost();
        String TRACKER_ID = Apps.get("GOOGLE_ANALYTICS_ID");
        if(TRACKER_ID != null && host.equals(Apps.get("PRODUCTION_HOST"))){
            GoogleAnalyticsTracker gaTracker = new GoogleAnalyticsTracker(TRACKER_ID, host);
            gaTracker.extend(UI.getCurrent()); // IT is an extension add-on for UI so it is initialized by calling .extend(UI)
        }
    }

    protected void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // Since there's no conditions in switching between the views
                // we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {

                DefaultViewItem itemView = menu.getItemViews().get(event.getViewName());

                // Appropriate events get fired after the view is changed.
                DefaultEventBus.post(new DefaultEvents.PostViewChangeEvent(itemView));

                DefaultEventBus.post(new DefaultEvents.BrowserResizeEvent());
                DefaultEventBus.post(new DefaultEvents.CloseOpenWindowsEvent());

                if (gaTracker!= null) {
                    // The view change is submitted as a pageview for GA tracker
                    gaTracker.trackPageview(VaadinService.getCurrentRequest().getContextPath() + "/" +event.getViewName());
                }
            }
        });
    }

    protected void initViewProviders() {
        // A dedicated view provider is added for each separate view type
        for (DefaultViewItem viewItem : menu.getItemViews().values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(viewItem.getViewName(), viewItem.getViewClass()) {

                // This field caches an already initialized view instance if the
                // view should be cached (stateful views).
                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewItem.getViewName().equals(viewName)) {
                        if (viewItem.isStateful()) {
                            // Stateful views get lazily instantiated
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewItem.getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            // Non-stateful views get instantiated every time
                            // they're navigated to
                            result = super.getView(viewItem.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewItem == HOME_ITEM_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return HOME_ITEM_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(HOME_ITEM_VIEW.getViewName());
            }
        });
    }
}