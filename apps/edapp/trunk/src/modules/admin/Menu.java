package modules.admin;

import apps.info.workset.dedicada.view.pages.reports.ReportsView;
import apps.info.workset.dedicada.view.pages.sales.SalesView;
import apps.info.workset.dedicada.view.pages.schedule.ScheduleView;
import apps.info.workset.dedicada.view.pages.transactions.TransactionsView;
import com.vaadin.server.FontAwesome;
import org.futurepages.core.modules.ModuleMenu;
import org.futurepages.core.view.items.ViewItem;

public class Menu extends ModuleMenu {

	@Override
	protected ViewItem home() {
		return (item("users",       SalesView.class,   FontAwesome.CHECK,      false, null, null));
		//return null;
	}

	@Override
	protected void addItems() {
		add(item("users",       SalesView.class,   FontAwesome.CHECK,      false, null, null));
		add(item("logs", TransactionsView.class,   FontAwesome.ASTERISK,   false, null, null));
		add(item("params",    ReportsView.class,   FontAwesome.FILE_PDF_O, true,  null, null));
		add(item("profiles", ScheduleView.class,   FontAwesome.COFFEE,     false, null, null));
		add(item("modules",  ScheduleView.class,   FontAwesome.GAVEL,      false, null, null));
		add(item("roles",    ScheduleView.class,   FontAwesome.LANGUAGE,   false, null, null));
	}

}
