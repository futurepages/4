package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.entities.Transaction;
import apps.info.workset.dedicada.view.components.UserMenuBar;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import org.futurepages.apps.simple.SimpleMenu;
import org.futurepages.core.event.Eventizer;
import org.futurepages.core.view.ViewItemButton;
import org.futurepages.core.view.ViewItemMenu;

import java.util.Collection;

@SuppressWarnings({ "serial", "unchecked" })
public final class AppMenu extends SimpleMenu {

	@Override
	protected ViewItemMenu viewItemMenuHome() {
		return AppMenuItems.HOME;
	}

	@Override
	protected Component userMenu() {
		return new UserMenuBar(); //TODO bring to fpg3 with the picture as parameter.
		//if you change for...
//		return super.userMenu();  // returns simple login title with logout button
	}

	@Override
	protected Component customViewItemButton(ViewItemButton viewItemButton) {

		// Add drop target to reports button. You can drop elements from table to this link.
		if (viewItemButton.getView() == AppMenuItems.REPORTS) {
			DragAndDropWrapper reports = new DragAndDropWrapper(viewItemButton);
			reports.setDragStartMode(DragAndDropWrapper.DragStartMode.NONE);
			reports.setDropHandler(new DropHandler() {

				@Override
				public void drop(final DragAndDropEvent event) {
					UI.getCurrent().getNavigator().navigateTo(viewItemButton.getView().getViewName());
					Table table = (Table) event.getTransferable().getSourceComponent();
					Eventizer.post(new AppEvents.TransactionReportEvent((Collection<Transaction>) table.getValue()));
				}

				@Override
				public AcceptCriterion getAcceptCriterion() {
					return AbstractSelect.AcceptItem.ALL;
				}
			});
			return reports;
		}
		return viewItemButton;
	}
}
