package org.futurepages.core.install;

import org.futurepages.core.config.Automations;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HibernateManager;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável pela automatização da instalação dos módulos.
 * Executa de forma reflexiva os 'Installers' de cada módulo.
 *
 * Os instaladores estão presente na raiz do pacote  install sob o seguinte nome
 * de classe: DataBaseInstaller
 *
 * @author leandro
 */
public class InstallersManager extends Automations {

	private static final String INSTALL_DIR_NAME = "model/install";
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
	 * See Futurepages2 docs and translate to new situation.
	 * */
	private void install(File[] modules) throws Exception {
		if (HibernateManager.isRunning() && modules != null && !installMode.equals("off")) {
			try {
				Dao.getInstance().beginTransaction();

				//Initial Resources Installer
//				try {
//					Class resourcesInstaller = Class.forName(INSTALL_DIR_NAME + ".Resources");
//					log(">>> installer " + resourcesInstaller.getSimpleName() + " running...  ");
//					resourcesInstaller.newInstance();
//					log(">>>   Resources OK.");
//				} catch (ClassNotFoundException ex) {
//					log(">>> installer of Resources isn't present.");
//				}

				//TODO rever este código... IMPLEMENTAR para o pacote 'apps'
//				if (installMode.startsWith("script:")) {
//					System.out.println("Installing data from script: "+Params.get("CLASSES_REAL_PATH") + "/install/" + installMode.split("script\\:")[1]+"...");
//					Dao.executeSQLs(false,FileUtil.getStringLines(Params.get("CLASSES_REAL_PATH")+ "/install/" + installMode.split("script\\:")[1]));
//				} else {
				if(installMode.equals("modules") || installMode.equals("on")){
					Map<String, List<Class<Installer>>> classes = getModulesDirectoryClasses(Installer.class, null);
					for (String moduleName : classes.keySet()) {
						log(moduleName + " installing...");
						for (Class<?> installer : classes.get(moduleName)) {
							if (!Modifier.isAbstract(installer.getModifiers())) {
								log(">>> installer " + installer.getSimpleName() + " running...  ");
								installer.newInstance();
								log(">>> installer " + installer.getSimpleName() + " OK");
							}
						}
						log(moduleName + " installed.");
					}
				}
// TODO IMPLEMENTAR O INSTALL DAS APPS FORA DOS MODULOS.
//					}
//					//ExtraInstaller
//					String extraInstaller = null;
//					if (!installMode.equals("modules")) {
//						if (installMode.equals("on")) {
//							extraInstaller = "Examples";
//						} else {
//							extraInstaller = The.capitalizedWord(installMode);
//						}
//					}

//					if (extraInstaller != null) {
//						try {
//							Class exInstallerClass = Class.forName(INSTALL_DIR_NAME + "." + extraInstaller);
//							log(">>> " + extraInstaller + " installing...  ");
//							Installer extras = (Installer) exInstallerClass.newInstance();
//							log(">>> " + extras + " installed in " + extras.totalTime() + " secs.");
//						} catch (ClassNotFoundException ex) {
//							log(">>> installer of " + extraInstaller + " not present.");
//						}
//					}
//				}
				Dao.getInstance().commitTransaction();
				Dao.close();

			} catch (Exception ex) {
				Dao.getInstance().rollBackTransaction();
				throw ex;
			}
		}
	}

	private void log(String msg) {
		System.out.println("[:install:] " + msg);
	}
}
