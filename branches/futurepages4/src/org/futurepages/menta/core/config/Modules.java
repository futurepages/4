package org.futurepages.menta.core.config;

import org.futurepages.core.config.Apps;
import org.futurepages.menta.core.ApplicationManager;
import org.futurepages.menta.core.control.AbstractApplicationManager;
import org.futurepages.util.The;

import java.io.File;

/**
 * Registrar os Módulos da aplicação
 */
//TODO ver como foi feito no jeito novo fora do menta e integrar.
public class Modules {
    /**
     * Registra o Gerenciador dos Módulos (ModuleManager) não comentado
     */
    public static void registerModule(ApplicationManager manager, File module) throws Exception {
        if (module.isDirectory()) {
            String moduleName = Apps.MODULES_PACK + "." + module.getName() + ".ModuleManager";
            File moduleManagerFile = new File(Apps.get("MODULES_CLASSES_REAL_PATH") + "/" + module.getName() + "/ModuleManager.class");
            //Registra o Manager do Módulo, caso ele exista.
            if (moduleManagerFile.exists()) {
                Class<? extends AbstractApplicationManager> moduleAppManager = (Class<? extends AbstractApplicationManager>) Class.forName(moduleName);
                manager.register(module.getName(), moduleAppManager);
            }
        }
    }

    /**
     * Registra os Gerenciadores de todos os Módulos (ModuleManager)
     * não comentados da aplicação
     */
    public static void registerAllModules(ApplicationManager manager, File[] modules) throws Exception {
        for (File module : modules) {
            registerModule(manager, module);
        }
    }

    /**
     * Registra somente os Gerenciadores dos Módulos (ModuleManager)
     * que acessam somente a base de dados local
     */
    public static void registerLocalModules(ApplicationManager manager, File[] modules) throws Exception {
        for (File module : modules) {
            if (!hasOwnSchema(module)) {
                registerModule(manager, module);
            }
        }
    }


    public static boolean hasOwnSchema(File module) {
        File hiberPropertiesFile = new File(The.concat(module.getAbsolutePath(), "/", Apps.CONFIGURATION_DIR_NAME, "/", Apps.BASE_HIBERNATE_PROPERTIES_FILE));
		return hiberPropertiesFile.exists();
    }
}