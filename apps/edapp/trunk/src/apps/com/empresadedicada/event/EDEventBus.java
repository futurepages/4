package apps.com.empresadedicada.event;

import apps.com.empresadedicada.AppUI;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class EDEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        AppUI.getEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        AppUI.getEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        AppUI.getEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
