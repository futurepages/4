package org.futurepages.core.control.vaadin;

import com.vaadin.ui.CustomComponent;

import java.util.LinkedHashMap;

public abstract class DefaultMenu  extends CustomComponent {

  private final LinkedHashMap<String, DefaultViewItem> itemViews = new LinkedHashMap<>();

  private DefaultViewItem HOME_ITEM_VIEW;

    public DefaultMenu(){
        HOME_ITEM_VIEW = homeViewItem();
        this.registerView(HOME_ITEM_VIEW);
        this.registerOtherItemViews();
    }


    protected void registerView(DefaultViewItem viewItem){
        itemViews.put(viewItem.getViewName(), viewItem);
    }

    protected DefaultViewItem getHomeItemView(){
        return HOME_ITEM_VIEW;
    }

    protected DefaultViewItem getItemView(String name){
        return itemViews.get(name);
    }

    protected abstract void registerOtherItemViews();
    protected abstract DefaultViewItem homeViewItem();

    public LinkedHashMap<String, DefaultViewItem> getItemViews() {
        return itemViews;
    }

}
