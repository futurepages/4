package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.entities.Transaction;
import apps.info.workset.dedicada.view.EDViewType;
import org.futurepages.core.control.vaadin.DefaultEvents;

import java.util.Collection;

public abstract class AppEvents extends DefaultEvents {

    public static class NotificationsCountUpdatedEvent {}

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

    }

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }

    public static final class PostViewChangeEvent {
        private final EDViewType view;

        public PostViewChangeEvent(final EDViewType view) {
            this.view = view;
        }

        public EDViewType getView() {
            return view;
        }
    }

    public static class ProfileUpdatedEvent {}

}