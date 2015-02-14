package org.futurepages.apps.common;

import com.vaadin.ui.CustomComponent;
import org.futurepages.core.view.ViewItem;

import java.util.LinkedHashMap;

public abstract class DefaultMenu  extends CustomComponent {

  private final LinkedHashMap<String, ViewItem> itemViews = new LinkedHashMap<>();

  private ViewItem HOME_ITEM_VIEW;

    public DefaultMenu(){
        HOME_ITEM_VIEW = homeViewItem();
        this.registerView(HOME_ITEM_VIEW);
        this.registerOtherItemViews();
    }


    protected void registerView(ViewItem viewItem){
        itemViews.put(viewItem.getViewName(), viewItem);
    }

    protected ViewItem getHomeItemView(){
        return HOME_ITEM_VIEW;
    }

    protected ViewItem getItemView(String name){
        return itemViews.get(name);
    }

    protected abstract void registerOtherItemViews();
    protected abstract ViewItem homeViewItem();

    public LinkedHashMap<String, ViewItem> getItemViews() {
        return itemViews;
    }

}
