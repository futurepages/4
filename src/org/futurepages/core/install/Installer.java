package org.futurepages.core.install;

import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.FileUtil;

import java.io.IOException;

/**
 * Instalador. Herde essa classe e coloque dentro do pacote install
 * do seu módulo caso queira instalar algo quando o MODE_INSTALL estiver ligado.
 * 
 * @author leandro
 */
public abstract class Installer implements Installation {

	private long timeCount;

	public Installer() {
		try {
			timeCount = System.currentTimeMillis();
			this.execute();
			timeCount = (System.currentTimeMillis() - timeCount) / 1000l;
		} catch (Exception ex) {
			System.out.println("[::install::] Error installing... " + this.getClass().getName());
			AppLogger.getInstance().execute(ex);
		}
	}

	protected void install(Installation installer) throws Exception {
		long initTime = System.currentTimeMillis() / 1000l;//em segundos
		String instaladorNome = installer.getClass().getName();

		System.out.println("     ----> Installing [" + instaladorNome + "]");
		try {
			installer.execute();
		} catch (Exception ex) {
			System.out.println("     ----> ERROR DURING INSTALLATION.");
			AppLogger.getInstance().execute(ex);
		}
		long endTime = (System.currentTimeMillis() / 1000l);
		System.out.println("     ----> installed in " + (endTime - initTime) + " secs.\n");
	}

	public long totalTime() {
		return this.timeCount;
	}

	protected void executeSQLFromFile(String path) throws IOException {
		String[] sqls = FileUtil.getStringLines(this.getClass(), path);
		Dao.getInstance().executeSQLs(true,sqls);
	}
}
