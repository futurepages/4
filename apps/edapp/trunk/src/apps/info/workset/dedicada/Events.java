package apps.info.workset.dedicada;

import apps.info.workset.dedicada.model.entities.Transaction;
import org.futurepages.core.event.NativeEvents;

import java.util.Collection;

public abstract class Events extends NativeEvents {

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }
}