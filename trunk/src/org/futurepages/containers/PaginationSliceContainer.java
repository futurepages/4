package org.futurepages.containers;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import org.futurepages.core.persistence.PaginationSlice;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.Is;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationSliceContainer<BEANTYPE> extends BeanContainer {

	private PaginationSlice<BEANTYPE> paginationSlice;
	private String originalSelect;
	private String originalWhere;
	private String originalOrder;
	private String newWhereFilter;
	private String newOrder;
    private String beanIdName;

    private Map<Object, BeanItem<BEANTYPE>> objects = new HashMap<>();

	public PaginationSliceContainer(PaginationSlice<BEANTYPE> paginationSlice) throws IllegalArgumentException {
		super(paginationSlice.getHqlQuery().getEntity());
        this.paginationSlice = paginationSlice;
        this.originalSelect  = paginationSlice.getHqlQuery().getSelect();
        this.originalWhere   = paginationSlice.getHqlQuery().getWhere();
        this.originalOrder   = paginationSlice.getHqlQuery().getOrder();
        this.beanIdName      =  (paginationSlice.getDao().getIdName(paginationSlice.getHqlQuery().getEntity()));
	}

	@Override
	public int size() {
        int size;
		if (!Is.empty(newWhereFilter)) {
            paginationSlice.getHqlQuery().setWhere(HQLProvider.ands(originalWhere, newWhereFilter));
            size = (int) paginationSlice.calcTotalSize();
		}else{
            size = (int) paginationSlice.getTotalSize().longValue();

        }
        return size;
    }

	@Override
	public BeanItem<BEANTYPE> getItem(Object itemId) {
		return objects.get(itemId);
	}

	@Override
	public synchronized List<BEANTYPE> getItemIds(int startIndex, int numberOfIds) {

        String theWhere = originalWhere;
        String theOrder = originalOrder;
        if (!Is.empty(newWhereFilter)) {
            theWhere = (HQLProvider.ands(originalWhere,newWhereFilter));
		}
        if(!Is.empty(newOrder)) {
            theOrder = (newOrder);
        }
        paginationSlice.getHqlQuery().setWhere(theWhere);
        paginationSlice.getHqlQuery().setOrder(theOrder);

        int i = startIndex;
        List allItemsIds = getAllItemIds();
        int itemsSize = allItemsIds.size();
        if(startIndex> itemsSize) {
            try {
                paginationSlice.getHqlQuery().setSelect(beanIdName);
                List beansIds = paginationSlice.loadRows(itemsSize, startIndex-itemsSize).getList();
                int k = itemsSize;
                for(Object id : beansIds){
                    allItemsIds.add(k, id);
                    k++;
                }
                paginationSlice.getHqlQuery().setSelect(originalSelect);
            } catch (Exception e) {
                paginationSlice.getHqlQuery().setSelect(originalSelect);
                throw new RuntimeException(e);
            }
        }

        paginationSlice.loadRows(startIndex, numberOfIds);
        List<BEANTYPE> listBeans = paginationSlice.getList();

        for(BEANTYPE bean : listBeans){
            Object beanId = paginationSlice.getDao().getIdValue(bean);
                allItemsIds.add(i, beanId);
            objects.put(beanId, new BeanItem(bean));
//            registerNewItem(i, beanId, new BeanItem(bean)); //only for listeners
            i++;
        }
        return super.getItemIds(startIndex,numberOfIds);
	}

    	public void applyFilter(String newWhereFilter){
            removeAllContainerFilters();
            removeAllItems();
            this.newWhereFilter = newWhereFilter;
    }

	public void removeFilter(){
        removeAllItems();
        removeAllContainerFilters();
        this.newWhereFilter = null;
    }

	public boolean hasItems() {
		return !(getVisibleItemIds().size() > 0);
	}

    @Override
    public void sort(Object[] propertyIds, boolean[] ascending) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < propertyIds.length; i++) {
            try {
                paginationSlice.getHqlQuery().getEntity().getDeclaredField(propertyIds[i].toString());
                sb.append((i!=0? "," : "")).append(ascending[i] ? HQLProvider.asc(propertyIds[i].toString()) : HQLProvider.desc(propertyIds[i].toString()));
                removeAllItems();
            } catch (NoSuchFieldException e) {
//                DefaultExceptionLogger.getInstance().execute(propertyIds[i] + "'s not a entity field! Just a method. Remove when ordering " + paginationSlice.getHqlQuery().getEntity().getName());
            }
        }
        newOrder = sb.toString();
    }
}