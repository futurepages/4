package org.futurepages.apps.simple;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.futurepages.core.config.Apps;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.modules.ModuleMenu;
import org.futurepages.core.view.items.ViewItem;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

@SuppressWarnings("serial")
public class SimpleNavigator extends Navigator {

    protected GoogleAnalyticsTracker gaTracker;
    protected ViewProvider errorViewProvider;
    private final ViewItem HOME_ITEM_VIEW;

    public SimpleNavigator(SimpleMenu menu, ComponentContainer container) {
        super(UI.getCurrent(), container);
        HOME_ITEM_VIEW = menu.getItemMenu().getHome();
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

                ViewItem itemView = SimpleUI.getCurrent().getMenus().getItemByName(event.getViewName());

                // Appropriate events get fired after the view is changed.
                Eventizer.post(new NativeEvents.PostViewChange(itemView));
                Eventizer.post(new NativeEvents.BrowserResize());
                Eventizer.post(new NativeEvents.CloseOpenWindows());

                if (gaTracker!= null) {
                    // The view change is submitted as a pageview for GA tracker
                    gaTracker.trackPageview(VaadinService.getCurrentRequest().getContextPath() + "/" +event.getViewName());
                }
            }
        });
    }

    protected void initViewProviders() {
        // A dedicated view provider is added for each separate view type
        for (ModuleMenu menu : SimpleUI.getCurrent().getMenus().list()) {
            for (ViewItem viewItem : menu.getViewItems()) {
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
                        SimpleUI.getCurrent().getMenu().navigateTo(viewItem.getViewName());
                        return result;
                    }
                };

                if (viewItem == HOME_ITEM_VIEW) {
                    errorViewProvider = viewProvider;
                }

                addProvider(viewProvider);
            }
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