package org.futurepages.core.install;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import org.futurepages.core.config.Params;

import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.tags.build.ModulesAutomation;
import org.futurepages.util.FileUtil;
import org.futurepages.util.The;

/**
 * Classe responsável pela automatização da instalação dos módulos.
 * Executa de forma reflexiva os 'Installers' de cada módulo.
 *
 * Os instaladores estão presente na raiz do pacote  install sob o seguinte nome
 * de classe: DataBaseInstaller
 *
 * @author leandro
 */
public class InstallersManager extends ModulesAutomation {

	private static final String INSTALL_DIR_NAME = "install";
	private String installMode;

	public InstallersManager(File[] modules, String installMode) {
		super(modules, INSTALL_DIR_NAME);
		this.installMode = installMode;
	}

	/**
	 * Inicializa os Instaladores do banco de dados.
	 *
	 * @throws java.lang.Exception
	 */
	public void install() throws Exception {
		log("BEGIN...");
		install(modules);
		log("END.");
	}

	/**
	 *
	 * @param modules são as pastas de módulos que serão varridas para instalação.
	 *
	 * @throws Exception
	 */
	public static void initialize(File[] modules, String installMode) throws Exception {
		new InstallersManager(modules, installMode).install();
	}

	/**
	 *
	 * Instalação do banco de dados.
	 *
	 * 1) Instala os Recursos (se houver: install.Resources)
	 * 2) Instala dentro de uma mesma transação:
	 * 3)	- Instala os módulos em ordem alfabética
	 * 4)	- Dependendo do modo de instalação...
	 *			- Instala os dados de exemplos da aplicação (se existir: install.Examples e (INSTALL_MODE = "examples" ou "on")
	 *			- Instala os dados de produção  da aplicação (se existir: install.Production e INSTALL_MODE = "production")
	 *			- Se estiver no modo "modules" não será instalado
	 * @param modules
	 * @throws Exception
	 * @throws java.lang.Exception
	 */
	private void install(File[] modules) throws Exception {
		if (modules != null && !installMode.equals("off")) {
			try {
				if (HibernateManager.isRunning()) {
					Dao.beginTransaction();
				}

				//Initial Resources Installer
				try {
					Class resourcesInstaller = Class.forName(INSTALL_DIR_NAME + ".Resources");
					log(">>> installer " + resourcesInstaller.getSimpleName() + " running...  ");
					resourcesInstaller.newInstance();
					log(">>>   Resources OK.");
				} catch (ClassNotFoundException ex) {
					log(">>> installer of Resources isn't present.");
				}

				if (installMode.startsWith("script:")) {
					System.out.println("Installing data from script: "+Params.get("CLASSES_REAL_PATH") + "/install/" + installMode.split("script\\:")[1]+"...");
					Dao.executeSQLs(false,FileUtil.getStringLines(Params.get("CLASSES_REAL_PATH")			   + "/install/" + installMode.split("script\\:")[1]));
				} else {
					Map<String, List<Class<Installer>>> classes = getModulesDirectoryClasses(Installer.class, null);
					for (String moduleName : classes.keySet()) {
						log("module '" + moduleName + "' installing...");
						for (Class<?> installer : classes.get(moduleName)) {
							if (!Modifier.isAbstract(installer.getModifiers())) {
								log(">>> installer " + installer.getSimpleName() + " running...  ");
								installer.newInstance();
								log(">>> installer " + installer.getSimpleName() + " OK");
							}
						}
						log("module '" + moduleName + "' installed.");
					}
					//ExtraInstaller
					String extraInstaller = null;
					if (!installMode.equals("modules")) {
						if (installMode.equals("on")) {
							extraInstaller = "Examples";
						} else {
							extraInstaller = The.capitalizedWord(installMode);
						}
					}

					if (extraInstaller != null) {
						try {
							Class exInstallerClass = Class.forName(INSTALL_DIR_NAME + "." + extraInstaller);
							log(">>> " + extraInstaller + " installing...  ");
							Installer extras = (Installer) exInstallerClass.newInstance();
							log(">>> " + extras + " installed in " + extras.totalTime() + " secs.");
						} catch (ClassNotFoundException ex) {
							log(">>> installer of " + extraInstaller + " not present.");
						}
					}
				}

				if (HibernateManager.isRunning()) {
					Dao.commitTransaction();
					Dao.close();
				}
			} catch (Exception ex) {
				Dao.rollBackTransaction();
				throw ex;
			}
		}
	}

	private void log(String msg) {
		System.out.println("[:install:] " + msg);
	}
}
