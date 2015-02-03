package org.futurepages.core.control.vaadin.containers;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.SimpleStringFilter;
import org.futurepages.core.pagination.PaginationSlice;
import org.futurepages.core.persistence.GenericDao;
import org.futurepages.core.persistence.HQLProvider;

import java.util.List;
import java.util.Set;

/**
 * @author Ondrej Kvasnovsky
 */
public class SliceContainer extends BeanContainer implements Container.Filterable, Container.Sortable {


//    private List<OrderByColumn> orderByColumns = new ArrayList<OrderByColumn>();
    private int minFilterLength;  // min filter string length, after this length is exceeded database calls are allowed
	private PaginationSlice paginationSlice;
	private GenericDao dao;
	private String order;
	private String newWhere;


    public SliceContainer(PaginationSlice paginationSlice) {
        super(paginationSlice.getHqlQuery().getEntity());
        minFilterLength = 3;
	    this.paginationSlice = paginationSlice;
	    this.dao = paginationSlice.getDao();
	    this.order = paginationSlice.getHqlQuery().getOrder();
    }

    @Override
    public int size() {
        filterStringToSearchCriteria();
//	    if (paginationSlice.getList().size() == 0 || this.get dao.session().isDirty()) {
            getCount();
//        } else if (isFiltered() && newWhere != null) {
            getCount();
//        }
        return Integer.parseInt(String.valueOf(paginationSlice.getTotalSize()));
    }

    private void getCount() {
	    paginationSlice.reloadPage();
    }

    @Override
    public BeanItem getItem(Object itemId) {
        return new BeanItem(itemId);
    }

    @Override
    public List<?> getItemIds(int startIndex, int numberOfIds) {
        filterStringToSearchCriteria();
        List<?> items = null;
        if (isFiltered() && newWhere != null) {
            items = findItems(startIndex, numberOfIds);
            newWhere = null;
        } else if (!isFiltered()) {
            items = findItems(startIndex, numberOfIds);
        }
        return items;
    }

    private List<?> findItems(int startIndex, int numberOfIds) {
	    paginationSlice.getHqlQuery().setOrder(order);
        if(newWhere!=null){
            paginationSlice.getHqlQuery().setWhere(newWhere);
        }
	    paginationSlice.setPageSize(numberOfIds);
        return paginationSlice.loadPageByFirstResult(startIndex).getList();
    }

    private void filterStringToSearchCriteria() {
        if (isFiltered()) {
            Set<Filter> filters = getFilters();
            for (Filter filter : filters) {
                if (filter instanceof SimpleStringFilter) {
                    SimpleStringFilter stringFilter = (SimpleStringFilter) filter;
                    String filterString = stringFilter.getFilterString();
                    if (filterString.length() > minFilterLength) {
                        this.newWhere = filterString;
                    } else {
                        this.newWhere = null;
                    }
                }
            }
        }
    }

    @Override
    public void sort(Object[] propertyIds, boolean[] ascending) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < propertyIds.length; i++) {
            sb.append((i!=0? ",":"")).append(ascending[i] ? HQLProvider.asc(propertyIds[i].toString()) : HQLProvider.desc(propertyIds[i].toString()));
        }

    }

    @Override
    public boolean containsId(Object itemId) {
        // we need this because of value change listener (otherwise selected item event won't be fired)
        return true;
    }

    public int getMinFilterLength() {
        return minFilterLength;
    }

    public void setMinFilterLength(int minFilterLength) {
        this.minFilterLength = minFilterLength;
    }
}
