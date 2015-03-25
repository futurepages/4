package org.futurepages.core.modules;

import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import org.futurepages.core.view.items.SimpleViewItem;
import org.futurepages.core.view.items.ViewItem;
import org.futurepages.core.view.items.ViewItemButtonCustomizer;
import org.futurepages.core.view.items.ViewItemMenu;
import org.futurepages.core.view.items.ViewItemNotifier;
import org.futurepages.util.ModuleUtil;

import java.util.Collection;
import java.util.LinkedHashMap;

public abstract class ModuleMenu implements ViewItemMenu {

	ViewItem home = null;

	private String modulePath = "";

	private LinkedHashMap<String, ViewItem> items = new LinkedHashMap<>();

	protected abstract ViewItem home();
	protected abstract void addItems();

	public ModuleMenu(){
		String moduleId = ModuleUtil.moduleId(this.getClass());
		if(!ModuleUtil.isAppId(moduleId)){
			modulePath = moduleId+"/";
		}

		this.home = home();

		if(hasHome()){
			add(this.home);
		}
		addItems();
	}

	protected void add(ViewItem viewItem){
		items.put(viewItem.getViewName(),viewItem);

	}

	protected ViewItem item(final String localizedViewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful, ViewItemNotifier notifier, ViewItemButtonCustomizer customButton){
		return new SimpleViewItem((modulePath+localizedViewName) , viewClass, icon, stateful, notifier, customButton);
	}

	public boolean hasHome(){
		return this.home!=null;
	}


	@Override
	public ViewItem getHome() {
		if(hasHome()) {
			return home;
		}
		return null;
	}


	@Override
	public Collection<ViewItem> getViewItems() {
		return items.values();
	}

	@Override
	public ViewItem getByName(String viewName) {
		return items.get(viewName);
	}

	public ViewItem get(String localViewId){
		return items.get(modulePath+localViewId);
	}
}
