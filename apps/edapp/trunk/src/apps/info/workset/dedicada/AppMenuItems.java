package apps.info.workset.dedicada;

import apps.info.workset.dedicada.view.pages.dashboard.HomeView;
import apps.info.workset.dedicada.view.pages.reports.ReportsView;
import apps.info.workset.dedicada.view.pages.sales.SalesView;
import apps.info.workset.dedicada.view.pages.schedule.ScheduleView;
import apps.info.workset.dedicada.view.pages.transactions.TransactionsView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import org.futurepages.core.view.ViewItem;
import org.futurepages.core.view.ViewItemMenu;

public enum AppMenuItems implements ViewItemMenu {

	HOME        ("home",         HomeView.class,         FontAwesome.HOME,        true, true){
		@Override
		public int getCountNotifications() {
			return AppUI.getDataProvider().getUnreadNotificationsCount();
		}
	},

	SALES       ("sales",        SalesView.class,        FontAwesome.BAR_CHART_O, false),
	TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE,       false),
	REPORTS     ("reports",      ReportsView.class,      FontAwesome.FILE_TEXT_O, true, true),
	SCHEDULE    ("schedule",     ScheduleView.class,     FontAwesome.CALENDAR_O,  false),
	;

	private String viewName;
	private Class<? extends View> viewClass;
	private Resource icon;
	boolean stateful; //if true, the view will be cached.
	boolean notifier; //if true, the item menu has a notify counter over it.

	AppMenuItems(String viewName, Class<? extends View> viewClass, Resource icon, boolean stateful) {
		this(viewName, viewClass, icon, stateful, false);
	}
	AppMenuItems(String viewName, Class<? extends View> viewClass, Resource icon, boolean stateful, boolean notifier) {
		this.viewName = viewName;
		this.viewClass = viewClass;
		this.icon = icon;
		this.stateful = stateful;
		this.notifier = notifier;
	}

	@Override
	public ViewItem getByName(String viewName) {
		for(ViewItem viewItem : values()){
			if(viewItem.getViewName().equals(viewName)){
				return viewItem;
			}
		}
		return null;
	}


	@Override
	public ViewItemMenu getHome() {
		return HOME;
	}

	@Override
	public ViewItem[] getViewItems() {
		return values();
	}

	@Override
	public String getViewName() {
		return this.viewName;
	}

	@Override
	public Class<? extends View> getViewClass() {
		return this.viewClass;
	}

	@Override
	public Resource getIcon() {
		return this.icon;
	}

	@Override
	public boolean isStateful() {
		return this.stateful;
	}
	public boolean isNotifier() {
		return this.notifier;
	}

	@Override
	public int getCountNotifications() {
		return 0;
	}
}
