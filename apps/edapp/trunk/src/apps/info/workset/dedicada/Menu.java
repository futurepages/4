package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.data.dummy.DummyDataProvider;
import apps.info.workset.dedicada.model.entities.Transaction;
import apps.info.workset.dedicada.view.pages.dashboard.HomeView;
import apps.info.workset.dedicada.view.pages.reports.ReportsView;
import apps.info.workset.dedicada.view.pages.sales.SalesView;
import apps.info.workset.dedicada.view.pages.schedule.ScheduleView;
import apps.info.workset.dedicada.view.pages.transactions.TransactionsView;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.modules.ModuleMenu;
import org.futurepages.core.view.items.ViewItem;
import org.futurepages.core.view.items.ViewItemButton;
import org.futurepages.core.view.items.ViewItemButtonCustomizer;
import org.futurepages.core.view.items.ViewItemNotifier;

import java.util.Collection;

public class Menu extends ModuleMenu {

	public static final String HOME         = "home";
	public static final String SALES        = "sales";
	public static final String TRANSACTIONS = "transactions";
	public static final String REPORTS      = "reports";
	public static final String SCHEDULE     = "schedule";

	@Override
	public ViewItem home() {
		return item(HOME, HomeView.class, FontAwesome.HOME, true, notifyHome(), null);
	}

	private ViewItemNotifier notifyHome() {
		return () -> DummyDataProvider.getInstance().getUnreadNotificationsCount();
	}

	@Override
	protected void addItems() {
		add(item(SALES,        SalesView.class,        FontAwesome.BAR_CHART_O, false, null, null));
		add(item(TRANSACTIONS, TransactionsView.class, FontAwesome.TABLE,       false, null, null));
		add(item(REPORTS,      ReportsView.class,      FontAwesome.FILE_TEXT_O, true,  null, this::customViewItemButton));
		add(item(SCHEDULE,     ScheduleView.class,     FontAwesome.CALENDAR_O,  false, null, null));
	}

	private Component customViewItemButton(ViewItemButton viewItemButton) {
		DragAndDropWrapper reports = new DragAndDropWrapper(viewItemButton);
		reports.setDragStartMode(DragAndDropWrapper.DragStartMode.NONE);
		reports.setDropHandler(new DropHandler() {

			@Override
			// Add drop target to reports button. You can drop elements from table to this link.
			public void drop(final DragAndDropEvent event) {
				UI.getCurrent().getNavigator().navigateTo(viewItemButton.getView().getViewName());
				Table table = (Table) event.getTransferable().getSourceComponent();
				Eventizer.post(new Events.TransactionReportEvent((Collection<Transaction>) table.getValue()));
			}

			@Override
			public AcceptCriterion getAcceptCriterion() {
				return AbstractSelect.AcceptItem.ALL;
			}
		});
		return reports;
	}
}