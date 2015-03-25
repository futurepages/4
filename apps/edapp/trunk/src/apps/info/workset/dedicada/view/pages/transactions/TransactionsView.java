package apps.info.workset.dedicada.view.pages.transactions;

import apps.info.workset.dedicada.Events;
import apps.info.workset.dedicada.model.entities.Transaction;
import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import modules.global.model.entities.brasil.Cidade;
import org.futurepages.core.event.Eventizer;
import org.futurepages.containers.PaginationSliceContainer;
import org.futurepages.core.event.NativeEvents;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLField;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;
import org.vaadin.maddon.FilterableListContainer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

@SuppressWarnings({ "serial", "unchecked" })
public final class TransactionsView extends VerticalLayout implements View {

    private final Table table;
    private Button createReport;

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private static final String[] DEFAULT_COLLAPSIBLE = { "pais" };

    public TransactionsView() {
        setSizeFull();
        addStyleName("transactions");
        Eventizer.register(this);

        addComponent(buildToolbar());

        table = buildTable();
        addComponent(table);
        setExpandRatio(table, 1);
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        Eventizer.unregister(this);
    }

    private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label title = new Label("Cidades Brasileiras");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);

        createReport = buildCreateReport();
        HorizontalLayout tools = new HorizontalLayout(buildFilter(), createReport);
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    private Button buildCreateReport() {
        final Button createReport = new Button("Create Report");
        createReport
                .setDescription("Create a new report from the selected transactions");
        createReport.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                createNewReportFromSelection();
            }
        });
        createReport.setEnabled(false);
        return createReport;
    }

    private Component buildFilter() {
        final TextField filter = new TextField();
        filter.addTextChangeListener(event -> {
            PaginationSliceContainer data =((PaginationSliceContainer) table.getContainerDataSource());
            	if (Is.empty(event.getText())) {
                    data.removeFilter();
				} else {
                    data.removeFilter();
					data.applyFilter(HQLProvider.ors(new HQLField("estado.nome").matches(event.getText()), new HQLField("nome").matches(event.getText()),new HQLField("nomeBusca").matches(event.getText())));
				}
				table.refreshRowCache();
				table.markAsDirty();
                table.setColumnFooter("pais", String.valueOf(data.size()));
        });

        filter.setInputPrompt("Filter");

        filter.setIcon(FontAwesome.SEARCH);
        filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
            @Override
            public void handleAction(final Object sender, final Object target) {
                if(!filter.getValue().equals("")){
                    filter.setValue("");
                    // ((Filterable) table.getContainerDataSource()).removeAllContainerFilters(); // este parece ser o pattern
                    ((PaginationSliceContainer) table.getContainerDataSource()).removeFilter();
                    table.refreshRowCache();
                    table.markAsDirty();
                    table.setColumnFooter("pais", String.valueOf(table.getContainerDataSource().size()));
                }
            }
        });
        return filter;
    }

    private Table buildTable() {
        final Table table = new Table();
//        {
//            @Override
//            protected String formatPropertyValue(final Object rowId,final Object colId, final Property<?> property) {
//                String result = super.formatPropertyValue(rowId, colId, property);
////                if(colId.equals("estado")){
////                    Estado estado = ((Cidade)(((BeanItem) getItem(rowId)).getBean())).getEstado();
////                    return (estado!=null)?estado.getNome(): "";
////                }
//                return result;
//            }
//        };

        table.setCacheRate(4);
        table.setSizeFull();
        table.addStyleName(ValoTheme.TABLE_BORDERLESS);
        table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        table.addStyleName(ValoTheme.TABLE_COMPACT);
        table.setSelectable(true);

        table.setColumnCollapsingAllowed(true);
        table.setPageLength(200);

        //table.setEditable(true);

        table.setColumnReorderingAllowed(true);

        //HbnContainer<Cidade> hbnContainer = new HbnContainer(Cidade.class);
        //hbnContainer.getItemIds();
        //table.setContainerDataSource(new BeanItemContainer(Cidade.class, Dao.list(Cidade.class)));
        //table.setContainerDataSource(new SliceContainer(pagCidades));

        PaginationSlice<Cidade> pagCidades = Dao.getInstance().paginationSlice(HQLProvider.hql(Cidade.class, HQLProvider.field("pais").equalsTo("BRA")));
        table.setContainerDataSource(new PaginationSliceContainer(pagCidades));
        table.setSortContainerPropertyId("nome");
        table.setSortAscending(true);

//        table.setColumnAlignment("seats", Align.RIGHT);
//        table.setColumnAlignment("price", Align.RIGHT);

//        table.setVisibleColumns("nomeBusca","nome","estado","pais");
//        table.setColumnHeaders("Cidade", "Nome", "Estado (Quando no Brasil)", "Capital do Estado", "País");
        table.setColumnHeaderMode(Table.ColumnHeaderMode.EXPLICIT_DEFAULTS_ID);
        table.setFooterVisible(true);
        table.setColumnFooter("nome", "Total");
//
        table.setColumnFooter("pais", String.valueOf(pagCidades.calcTotalSize()));

        // Allow dragging items to the reports menu
        table.setDragMode(TableDragMode.MULTIROW);
        table.setMultiSelect(true);

        table.addActionHandler(new TransactionsActionHandler());

        table.addValueChangeListener(event -> {
            if (table.getValue() instanceof Set) {
//                Set<Object> val = (Set<Object>) table.getValue();
//                createReport.setEnabled(val.size() > 0);
                        table.setColumnFooter("pais", String.valueOf(table.getContainerDataSource().size()));
            }
        });
        table.setImmediate(true);

        return table;
    }

    private boolean defaultColumnsVisible() {
        boolean result = true;
        for (String propertyId : DEFAULT_COLLAPSIBLE) {
            if (table.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
                result = false;
            }
        }
        return result;
    }

    @Subscribe
    public void browserResized(final NativeEvents.BrowserResize event) {
        // Some columns are collapsed when browser window width gets small
        // enough to make the table fit better.
        if (defaultColumnsVisible()) {
            for (String propertyId : DEFAULT_COLLAPSIBLE) {
                table.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
            }
        }
    }

    private boolean filterByProperty(final String prop, final Item item,
            final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    void createNewReportFromSelection() {
        UI.getCurrent().getNavigator().navigateTo("reports");
        Eventizer.post(new Events.TransactionReportEvent((Collection<Transaction>) table.getValue()));
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class TransactionsActionHandler implements Handler {
        private final Action report = new Action("Create Report");

        private final Action discard = new Action("Discard");

        private final Action details = new Action("Movie details");

        @Override
        public void handleAction(final Action action, final Object sender,final Object target) {
//            if (action == report) {
//                createNewReportFromSelection();
//            } else if (action == discard) {
//                Notification.show("Not implemented in this demo");
//            } else if (action == details) {
//                Item item = ((Table) sender).getItem(target);
//                if (item != null) {
//                    Long movieId = (Long) item.getItemProperty("movieId")
//                            .getValue();
//                    MovieDetailsWindow.open(AppUI.getDataProvider().getMovie(movieId), null, null);
//                }
//            }
        }

        @Override
        public Action[] getActions(final Object target, final Object sender) {
            return new Action[] { details, report, discard };
        }
    }

    private class TempTransactionsContainer extends FilterableListContainer<Transaction> {

        public TempTransactionsContainer(
                final Collection<Transaction> collection) {
            super(collection);
        }

        // This is only temporarily overridden until issues with
        // BeanComparator get resolved.
        @Override
        public void sort(final Object[] propertyId, final boolean[] ascending) {
            final boolean sortAscending = ascending[0];
            final Object sortContainerPropertyId = propertyId[0];
            Collections.sort(getBackingList(), new Comparator<Transaction>() {
                @Override
                public int compare(final Transaction o1, final Transaction o2) {
                    int result = 0;
                    if ("time".equals(sortContainerPropertyId)) {
                        result = o1.getTime().compareTo(o2.getTime());
                    } else if ("country".equals(sortContainerPropertyId)) {
                        result = o1.getCountry().compareTo(o2.getCountry());
                    } else if ("city".equals(sortContainerPropertyId)) {
                        result = o1.getCity().compareTo(o2.getCity());
                    } else if ("theater".equals(sortContainerPropertyId)) {
                        result = o1.getTheater().compareTo(o2.getTheater());
                    } else if ("room".equals(sortContainerPropertyId)) {
                        result = o1.getRoom().compareTo(o2.getRoom());
                    } else if ("title".equals(sortContainerPropertyId)) {
                        result = o1.getTitle().compareTo(o2.getTitle());
                    } else if ("seats".equals(sortContainerPropertyId)) {
                        result = new Integer(o1.getSeats()).compareTo(o2
                                .getSeats());
                    } else if ("price".equals(sortContainerPropertyId)) {
                        result = new Double(o1.getPrice()).compareTo(o2
                                .getPrice());
                    }

                    if (!sortAscending) {
                        result *= -1;
                    }
                    return result;
                }
            });
        }

    }

}
