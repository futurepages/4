package apps.com.empresadedicada;

import apps.com.empresadedicada.event.EDEvent.BrowserResizeEvent;
import apps.com.empresadedicada.event.EDEvent.CloseOpenWindowsEvent;
import apps.com.empresadedicada.event.EDEvent.PostViewChangeEvent;
import apps.com.empresadedicada.event.EDEventBus;
import apps.com.empresadedicada.view.EDViewType;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import org.futurepages.core.config.Apps;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

@SuppressWarnings("serial")
public class AppNavigator extends Navigator {


    private static final EDViewType ERROR_VIEW = EDViewType.DASHBOARD;
    private ViewProvider errorViewProvider;

    public AppNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);

        String host = getUI().getPage().getLocation().getHost();
        String TRACKER_ID = Apps.get("GOOGLE_ANALYTICS_ID");
        if (TRACKER_ID != null && host.equals(Apps.get("PRODUCTION_HOST"))) {
            initGATracker(TRACKER_ID);
        }
        initViewChangeListener();
        initViewProviders();

    }

    private void initGATracker(final String trackerId) {
        GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker(trackerId, "demo.vaadin.com");

        // GoogleAnalyticsTracker is an extension add-on for UI so it is initialized by calling .extend(UI)
        tracker.extend(UI.getCurrent());
    }

    private void initViewChangeListener() {
        addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeEvent event) {
                // Since there's no conditions in switching between the views
                // we can always return true.
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeEvent event) {
                EDViewType view = EDViewType.getByViewName(event
                        .getViewName());
                // Appropriate events get fired after the view is changed.
                EDEventBus.post(new PostViewChangeEvent(view));
                EDEventBus.post(new BrowserResizeEvent());
                EDEventBus.post(new CloseOpenWindowsEvent());

//                if (tracker != null) {
//                    // The view change is submitted as a pageview for GA tracker
//                    tracker.trackPageview("/dashboard/" + event.getViewName());
//                }
            }
        });
    }

    private void initViewProviders() {
        // A dedicated view provider is added for each separate view type
        for (final EDViewType viewType : EDViewType.values()) {
            ViewProvider viewProvider = new ClassBasedViewProvider(
                    viewType.getViewName(), viewType.getViewClass()) {

                // This field caches an already initialized view instance if the
                // view should be cached (stateful views).
                private View cachedInstance;

                @Override
                public View getView(final String viewName) {
                    View result = null;
                    if (viewType.getViewName().equals(viewName)) {
                        if (viewType.isStateful()) {
                            // Stateful views get lazily instantiated
                            if (cachedInstance == null) {
                                cachedInstance = super.getView(viewType
                                        .getViewName());
                            }
                            result = cachedInstance;
                        } else {
                            // Non-stateful views get instantiated every time
                            // they're navigated to
                            result = super.getView(viewType.getViewName());
                        }
                    }
                    return result;
                }
            };

            if (viewType == ERROR_VIEW) {
                errorViewProvider = viewProvider;
            }

            addProvider(viewProvider);
        }

        setErrorProvider(new ViewProvider() {
            @Override
            public String getViewName(final String viewAndParameters) {
                return ERROR_VIEW.getViewName();
            }

            @Override
            public View getView(final String viewName) {
                return errorViewProvider.getView(ERROR_VIEW.getViewName());
            }
        });
    }
}
