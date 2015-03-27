package modules.global;

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
		return null;
	}

	@Override
	protected void addItems() {
		add(item("cidades",       SalesView.class,   FontAwesome.PAGELINES,      false, null, null));
		add(item("paises", TransactionsView.class,   FontAwesome.BITBUCKET,   false, null, null));
		add(item("orgaos",    ReportsView.class,   FontAwesome.CHECK_CIRCLE, true,  null, null));
		add(item("tipos", ScheduleView.class,   FontAwesome.BACKWARD,     false, null, null));
	}

}
