package apps.info.workset.dedicada;

import apps.info.workset.dedicada.view.EDViewType;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import org.futurepages.core.control.vaadin.DefaultEventBus;
import org.futurepages.core.control.vaadin.DefaultNavigator;

@SuppressWarnings("serial")
public class AppNavigator extends DefaultNavigator {

    private static final EDViewType ERROR_VIEW = EDViewType.DASHBOARD;
    private ViewProvider errorViewProvider;

    public AppNavigator(ComponentContainer container) {
        super(container);
    }

    @Override
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
                EDViewType view = EDViewType.getByViewName(event
                        .getViewName());
                // Appropriate events get fired after the view is changed.
                DefaultEventBus.post(new AppEvents.PostViewChangeEvent(view));
                DefaultEventBus.post(new AppEvents.BrowserResizeEvent());
                DefaultEventBus.post(new AppEvents.CloseOpenWindowsEvent());

//                if (tracker != null) {
//                    // The view change is submitted as a pageview for GA tracker
//                    tracker.trackPageview("/dashboard/" + event.getViewName());
//                }
            }
        });
    }

    @Override
    protected void initViewProviders() {
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