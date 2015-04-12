package org.futurepages.core.modules;

import com.vaadin.ui.UI;
import org.futurepages.apps.simple.SimpleUI;
import org.futurepages.core.auth.DefaultModule;
import org.futurepages.core.auth.DefaultUser;
import org.futurepages.core.view.items.ViewItem;
import org.futurepages.util.ModuleUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Menus {

	private HashMap<String, ModuleMenu> mapMenus = new HashMap<>();
	private HashMap<String, ViewItem>   mapItems = new HashMap<>();

	public Menus(DefaultUser loggedUser){
		Map<String,String> menuMap = new HashMap<>();
		for(String appPack : ModuleUtil.getAppsList()){
			menuMap.put(ModuleUtil.moduleId(appPack), appPack);
		}
		if(loggedUser!=null){
			for(DefaultModule module  : loggedUser.getModules()){
				menuMap.put(module.getModuleId(), ModuleUtil.getPackageByModuleId(module.getModuleId()));
			}
		}
		for(String moduleId : menuMap.keySet()){
			String menuQualifiedName = menuMap.get(moduleId)+".Menu";
			try {
				Class<? extends ModuleMenu> menuClass = (Class<? extends ModuleMenu>) Class.forName(menuQualifiedName);
				ModuleMenu moduleMenu = menuClass.newInstance();
				mapMenus.put(moduleId, moduleMenu);
				for(ViewItem viewItem : moduleMenu.getViewItems()){
					mapItems.put(viewItem.getViewName(), viewItem);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				System.err.println("[Menus]: "+menuQualifiedName+" not instance of ModuleMenu");
			} catch(ClassNotFoundException ignored){}
		}
	}

	public ViewItem getItemByName(String namePath){
		return mapItems.get(namePath);
	}

	public static ModuleMenu get(Object caller){
		String moduleId;
		if(caller instanceof String){
			moduleId = (String) caller;
		}else{
			moduleId = ModuleUtil.moduleId(caller.getClass());
		}
		return SimpleUI.getCurrent().getMenus().mapMenus.get(moduleId);
	}

	public  Collection<ModuleMenu> list(){
		return mapMenus.values();
	}
}
