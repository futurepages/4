package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.entities.Transaction;
import org.futurepages.core.control.vaadin.Events;

import java.util.Collection;

public abstract class AppEvents extends Events {

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

    public static class ProfileUpdatedEvent {}

}