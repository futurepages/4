package org.futurepages.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.futurepages.core.config.Modules;
import org.futurepages.core.config.Params;
import org.futurepages.core.control.AbstractApplicationManager;
import org.futurepages.core.context.Context;
import org.futurepages.core.control.AbstractModuleManager;

/**
 * Classe onde são feitas as inicializações da aplicação.
 * Sao registrados (agregados) todos os ApplicationManagers dos Módulos da Aplicação (ModuleManagers).
 */
public class ApplicationManager extends AbstractApplicationManager {

	private final Map<String, AbstractApplicationManager> managers;
	private Set<String> moduleIDs = new HashSet<String>();
	private Map<String, LinkedHashSet<String>> moduleDependencies;
	private Map<String, HashSet<String>> subModules;
	private boolean initialized = false;

	/**
	 * Default constructor, capable to call the registerManagers() method.
	 *
	 * @throws Exception  about the ApplicationManager instanciation
	 */
	public ApplicationManager() {
		super();
		this.managers = new HashMap<String, AbstractApplicationManager>();
		registerManagers();
		initialized = true;
	}

	/**
	 * Registra o InitManager e os ModuleManagers dos demais módulos
	 */
	public void registerManagers() {
		try {
			log("Iniciando Registro de Managers");
			//Lista os módulos na árvore de arquivos
			File[] modules = (new File(Params.get("MODULES_CLASSES_REAL_PATH"))).listFiles();

			if (modules != null) {
				for (File module : modules) {
					moduleIDs.add(module.getName());
				}
			}

			//Registra o Módulo de Inicialização da Aplicação
			Class initManagerClass = Class.forName(Params.get("INIT_MANAGER_CLASS"));
			register("", initManagerClass);

			// Registra os demais módulos: mapeia, registra os managers e conecta ao banco criando uma sessão.
			if (modules != null) {
				if (Params.get("CONNECT_EXTERNAL_MODULES").equals("false")) {
					Modules.registerLocalModules(this, modules);
				} else if (Params.get("CONNECT_EXTERNAL_MODULES").equals("true")) {
					Modules.registerAllModules(this, modules);
				}
			}
			if (Params.get("USE_MODULE_DEPENDENCY").equals("true")) {
				moduleDependencies = new HashMap<String, LinkedHashSet<String>>();
				List<AbstractModuleManager> integrationModules = new ArrayList();
				for (AbstractApplicationManager m : managers.values()) {
					if (m instanceof AbstractModuleManager) {
						AbstractModuleManager mm = (AbstractModuleManager) m;
						moduleDependencies.put(mm.getModuleId(), loadDependencies(mm));
						if (mm.isIntegrationModule()) {
							integrationModules.add(mm);
						}
					}
				}
				for (AbstractModuleManager im : integrationModules) {
					for (String moduleId : moduleDependencies.keySet()) {
						if(!moduleId.equals(im.getModuleId())){
							LinkedHashSet<String> moduleDependenciesAux = new LinkedHashSet<String>();
							moduleDependenciesAux.add(im.getModuleId());
							moduleDependenciesAux.addAll(moduleDependencies.get(moduleId));
							moduleDependencies.put(moduleId, moduleDependenciesAux);
						}
					}
				}

				//for Test!!! @DEBUG-MODE
//				System.out.println("USE_MODULE_DEPENDENCY = TRUE ============");
//				for (AbstractApplicationManager m : managers.values()) {
//					if (m instanceof AbstractModuleManager) {
//						AbstractModuleManager mm = (AbstractModuleManager) m;
//						System.out.println("module '" + mm.getModuleId()+ "' depends of: ");
//						for(String dependency : moduleDependencies.get(mm.getModuleId())){
//								System.out.println("   - "+dependency);
//						}
//					}else{
//						System.out.println(">> Other Manager: "+m.getClass().getName());
//					}
//				}
//				System.out.println("=========================================");
			}
			log("Managers Iniciados");
		} catch (Exception ex) {
			log("Erro ao registrar os módulos do sistema: " + ex.getMessage());
		}
	}

	/**
	 * Call this method to register an ApplicationManager.
	 *
	 * @param manager The application manager to register.
	 */
	public void register(String moduleId, Class<? extends AbstractApplicationManager> manager) {

		if (initialized) {
			throw new IllegalStateException("MultiApplicationManager is already initialized! Call register from registerManagers() method!");
		}

		try {
			org.futurepages.core.control.AbstractApplicationManager newInstance = manager.newInstance();
			newInstance.setParent(this);
			this.managers.put(moduleId, newInstance);
		} catch (Exception e) {
			throw new RuntimeException("Unable to instanciate the class: "
					+ manager.getSimpleName() + ".  Read the next stack for details.", e);
		}
	}

	public Set<String> getDependenciesOf(String moduleId){
		return moduleDependencies.get(moduleId);
	}

	public boolean moduleHasSub(String module, String subModule){
		return subModules!=null && subModules.get(module)!=null && subModules.get(module).contains(subModule);
	}

	public void addSubModule(String module, String subModule){
		if(subModules==null){
			subModules = new HashMap<String, HashSet<String>>();
		}
		HashSet<String> theSubModulesOf = subModules.get(module);
		if(theSubModulesOf == null){
			subModules.put(module,new HashSet<String>());
			theSubModulesOf = subModules.get(module);
		}
		theSubModulesOf.add(subModule);
	}



	@Override
	public final void init(Context application) {
		super.init(application);
		for (AbstractApplicationManager manager : this.managers.values()) {
			manager.init(application);
		}
	}

	@Override
	public Set<String> moduleIds() {
		return moduleIDs;
	}

	@Override
	public final void loadActions() {
		super.loadActions();
		for (AbstractApplicationManager manager : this.managers.values()) {
			manager.loadActions();
		}
		super.afterLoadManagers();
	}

	public LinkedHashSet<String> loadDependencies(AbstractModuleManager mm) {
		if (mm.getDirectDependencies() == null) {
			mm.loadDependencies();
		}
		LinkedHashSet<String> dependencies = new LinkedHashSet<String>();
		if (mm.getDirectDependencies()==null || mm.getDirectDependencies().length == 0) {
			return dependencies;
		} else {
			for (String moduleId : mm.getDirectDependencies()) {
				dependencies.addAll(loadDependencies((AbstractModuleManager) managers.get(moduleId)));
				dependencies.add(moduleId);
			}
			return dependencies;
		}
	}

	@Override
	public final void loadLocales() {
		super.loadLocales();
		for (AbstractApplicationManager manager : this.managers.values()) {
			manager.loadLocales();
		}
	}

	@Override
	public final void loadFormatters() {
		super.loadFormatters();
		for (AbstractApplicationManager manager : this.managers.values()) {
			manager.loadFormatters();


		}
	}

	private void log(String msg) {
		System.out.println("[::appManager::] " + msg);

	}
}
