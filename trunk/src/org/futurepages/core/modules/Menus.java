package org.futurepages.core.modules;

import org.futurepages.core.view.items.ViewItem;
import org.futurepages.util.ModuleUtil;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class Menus {

	private HashMap<String, ModuleMenu> mapMenus = new HashMap<>();
	private HashMap<String, ViewItem>   mapItems = new HashMap<>();

	private static Menus INSTANCE;

	private Menus(File[] appsAndModules){
		for(File moduleDir : appsAndModules){
			String menuQualifiedName = ModuleUtil.getModulePackage(moduleDir)+".Menu";
			String moduleId = ModuleUtil.moduleId(moduleDir);
			try {
				Class<? extends ModuleMenu> menuClass = (Class<? extends ModuleMenu>) Class.forName(menuQualifiedName);
				ModuleMenu moduleMenu = menuClass.newInstance();
				mapMenus.put(moduleId, moduleMenu);
				for(ViewItem viewItem : moduleMenu.getViewItems()){
					mapItems.put(viewItem.getViewName(), viewItem);
				}
			} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
				System.out.println(menuQualifiedName+" not present or not instance of ModuleMenu");
			}
		}
	}

	public synchronized static void init(){
		init(ModuleUtil.listModulesAndApps());
	}

	public synchronized static void init(File[] appsAndModules){
		if (INSTANCE == null) {
			INSTANCE = new Menus(appsAndModules);
		}
	}

	public static Menus getInstance(){
		return INSTANCE;
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
		try{
			return getInstance().mapMenus.get(moduleId);
		}catch(NullPointerException ex){
			init();
			return getInstance().mapMenus.get(moduleId);
		}
	}

	public  Collection<ModuleMenu> list(){
		return mapMenus.values();
	}
}
